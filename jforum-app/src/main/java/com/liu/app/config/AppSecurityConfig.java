package com.liu.app.config;

import com.liu.security.config.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Description: 安全配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 14:28
 */
@Configuration
public class AppSecurityConfig {
    /**
     * 注入允许的路径
     *
     * @return 字符串数组
     */
    @Bean("allowAccess")
    public String[] allowAccess() {
        String[] defaultPaths = SecurityConfig.DEFAULT_ALLOW_ACCESS_PATHS;

        String[] customPaths = {
                "/app/*/api/**",
                "/app/api/**"
        };
        // 合并
        String[] allPaths = Arrays.copyOf(defaultPaths, defaultPaths.length + customPaths.length);
        System.arraycopy(customPaths, 0, allPaths, defaultPaths.length, customPaths.length);
        return allPaths;
    }
}
