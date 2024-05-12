package com.liu.app.service.impl;

import com.liu.app.factory.AsyncFactory;
import com.liu.app.service.LoginService;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.CacheConstants;
import com.liu.core.constant.Constants;
import com.liu.core.constant.UserConstants;
import com.liu.core.excption.ServiceException;
import com.liu.core.excption.user.*;
import com.liu.core.manager.AsyncManager;
import com.liu.core.model.BaseUser;
import com.liu.core.model.LoginUser;
import com.liu.core.utils.IpUtils;
import com.liu.core.utils.MessageUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysMenu;
import com.liu.db.entity.SysRole;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysConfigService;
import com.liu.db.service.SysUserService;
import com.liu.db.service.relation.SysRoleAndMenuService;
import com.liu.db.service.relation.SysUserAndRoleService;
import com.liu.security.context.AuthenticationContextHolder;
import com.liu.security.service.JwtTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 15:57
 */
@Service
public class LoginServiceImpl implements LoginService {
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

    @Override
    public String login(String username, String password, String captchaCode, String uuid, Boolean slider) {
        // 验证码校验
        validateCaptcha(username, captchaCode, uuid, slider);
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
        // 如果用户是前台用户则验证失败
        SysUser user = userService.getItemByUserName(username);
        if (user != null && !user.getUserType().equals(UserConstants.USER_TYPE)) {
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
     * @param slider
     */
    private void validateCaptcha(String username, String code, String uuid, Boolean slider) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + ":" + StringUtils.defaultIfEmpty(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.delObject(verifyKey);
            if (captcha == null) {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.captcha.expire")));
                throw new CaptchaExpireException();
            }
            // 普通验证码
            if (!code.equalsIgnoreCase(captcha) && Boolean.FALSE.equals(slider)) {
                AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.captcha.error")));
                throw new CaptchaException();
            }
            // 滑块验证码
            if (Boolean.TRUE.equals(slider)) {
                int blockX = Integer.parseInt(code);
                int cacheBlockX = Integer.parseInt(captcha);
                // 偏移量 大于1.5就是失败
                if (Math.abs(blockX - cacheBlockX) <= 2) {
                    AsyncManager.manager().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, MessageUtils.message("user.captcha.error")));
                    throw new CaptchaException();
                }
            }
        }
    }

}
