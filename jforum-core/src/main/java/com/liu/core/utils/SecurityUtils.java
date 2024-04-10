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
        // TODO 2024/4/10/17:08 记住后续要将这个关闭 这个只是为了测试
        String username = "JIE123";
        try {
            username = request.getUserPrincipal().getName();
        } catch (Exception ignored) {

        }
        return username;
    }
}
