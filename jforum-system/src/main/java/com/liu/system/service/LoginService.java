package com.liu.system.service;

import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.CacheConstants;
import com.liu.core.constant.Constants;
import com.liu.core.excption.ServiceException;
import com.liu.core.excption.user.*;
import com.liu.core.manager.AsyncManager;
import com.liu.core.model.BaseUser;
import com.liu.core.model.LoginUser;
import com.liu.core.utils.IpUtils;
import com.liu.core.utils.MessageUtils;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.security.context.AuthenticationContextHolder;
import com.liu.security.service.JwtTokenService;
import com.liu.system.constants.UserConstants;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.factory.AsyncFactory;
import com.liu.system.service.relation.SysRoleAndMenuService;
import com.liu.system.service.relation.SysUserAndRoleService;
import com.liu.system.vo.RegisterBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 19:06
 */
@Service
public class LoginService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private SysConfigService configService;

    /**
     * 用户登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                // 登录日志记录
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        BaseUser baseUser = new BaseUser();
        baseUser.setUsername(username);
        loginUser.setUser(baseUser);
        // 设置当前登录角色 默认 最高权限
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        List<SysRole> roleList = SpringUtils.getBean(SysUserAndRoleService.class).getRoleByUserId(user.getUserId());
        if (roleList.size() == 0) {
            loginUser.setCurrRole("游客");
        } else {
            loginUser.setCurrRole(roleList.get(0).getRoleName());
            // 获取该角色的权限
            loginUser.setPermissions(getPermissions(roleList.get(0).getRoleId()));
        }


        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 获取 权限标识
     *
     * @param roleId 角色ID
     * @return 返回权限标识列表
     */
    private Set<String> getPermissions(Long roleId) {
        Set<String> permissions = new HashSet<>();
        List<SysMenu> menus = SpringUtils.getBean(SysRoleAndMenuService.class).selectMenuListByRoleId(roleId);
        for (SysMenu menu : menus) {
            permissions.add(menu.getPerms());
        }
        return permissions;
    }


    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddress());
        sysUser.setLoginDate(new Date());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 校验 用户名 和 密码
     *
     * @param username 用户名
     * @param password 密码
     */
    private void loginPreCheck(String username, String password) {

        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddress())) {
            AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 验证 验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + ":" + StringUtils.defaultIfEmpty(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.delObject(verifyKey);
            if (captcha == null) {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.captcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.captcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 注册用户
     *
     * @param registerBody 注册用户参数
     */
    public String register(RegisterBody registerBody) {
        String message = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCaptchaCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            message = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            message = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            message = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            message = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            message = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean flag = userService.registerUser(sysUser);
            if (!flag) {
                message = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.REGISTER,
                        MessageUtils.message("user.register.success")));
            }
        }
        return message;
    }

    /**
     * 切换角色
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    public String switchRole(Long roleId, Long userId) {
        List<SysRole> roleList = SpringUtils.getBean(SysUserAndRoleService.class).getRoleByUserId(userId);
        for (SysRole role : roleList) {
            if (Objects.equals(role.getRoleId(), roleId)) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                loginUser.setCurrRole(role.getRoleName());
                // 切换角色  --> 权限列表变换
                loginUser.setPermissions(getPermissions(roleId));
                return tokenService.createToken(loginUser);
            }
        }
        return null;
    }
}
