package com.liu.security.component;

import com.liu.core.model.LoginUser;
import com.liu.core.utils.SpringUtils;
import com.liu.security.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 16:00
 */
//@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        JwtTokenService jwtTokenService = SpringUtils.getBean(JwtTokenService.class);
        // 获取当前登录用户信息 进行判断 验证Token是否过期
        LoginUser loginUser = jwtTokenService.getLoginUser(request);
        if (loginUser != null) {
            jwtTokenService.verifyToken(loginUser);
            // 构建 UsernamePasswordAuthenticationToken 这里密码为null 是因为提供了正确的token 实现自动登录
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.warn("登录用户:{}-->权限:{}", loginUser.getUsername(), loginUser.getAuthorities());
        }
        // 开启过滤
        filterChain.doFilter(request, response);
    }
}
