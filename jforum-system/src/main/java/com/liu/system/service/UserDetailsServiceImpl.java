package com.liu.system.service;

import com.liu.core.excption.ServiceException;
import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.model.BaseUser;
import com.liu.core.model.LoginUser;
import com.liu.core.utils.MessageUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.service.SysUserService;
import com.liu.security.context.AuthenticationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Description: 用户验证处理
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:14
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @SuppressWarnings("All")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserService userService = SpringUtils.getBean(SysUserService.class);
        BaseUser user = userService.selectSysUserByUserName(username);
        // log.warn("查询用户:{}", user);
        if (user == null) {
            log.info("登录用户 ::: {} 不存在. ", username);
            // 2024/2/17/18:59
            //  1.这里可以让一个类去实现 RuntimeException 返回自定义异常结果
            //  2.返回的信息也可以 国际化处理
            throw new UserNotExistsException();
        } else if (user.getStatus() == "1") {
            log.info("登录用户 ::: {} 已停用. ", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        // 将其设置为单例 就不会存在多个 Authentication
        Authentication authentication = AuthenticationContextHolder.getContext();
        String name = authentication.getName();
        // 明文密码
        String password = authentication.getCredentials().toString();
        // 密码验证
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new RuntimeException("密码不正确!" + password +
                    "原数据库中加密的密码:" + user.getPassword());
        }

//        log.warn("验证身份成功!");
        // 以上种种验证过后 身份验证完成
        return new LoginUser(user.getUserId(), user, userService.getMenuPermission(user));
    }
}