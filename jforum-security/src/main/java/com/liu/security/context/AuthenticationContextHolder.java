package com.liu.security.context;

import org.springframework.security.core.Authentication;

/**
 * Description: 身份验证
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:16
 */
public class AuthenticationContextHolder {

    /**
     * ThreadLocal 用于存储当前身份验证上下文
     */
    private static final ThreadLocal<Authentication> CONTEXT_HOLDER = new ThreadLocal<>();

    public static Authentication getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void setContext(Authentication context) {
        CONTEXT_HOLDER.set(context);
    }

    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }
}
