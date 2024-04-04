package com.liu.core.utils;

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
}
