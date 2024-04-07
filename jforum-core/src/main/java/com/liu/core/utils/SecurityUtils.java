package com.liu.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description: 安全工具
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 10:32
 */
public class SecurityUtils {

    /**
     * 加密 密码
     *
     * @param password 密码
     * @return 加密后的结果
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 获取 当前登录用户的 用户名
     */
    public static String getCurrentUser(HttpServletRequest request) {
        String username = "JIE123";
        try {
            username = request.getUserPrincipal().getName();
        } catch (Exception ignored) {

        }
        return username;
    }
}
