package com.liu.security.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Description: 权限信息 此代码正在实现一个 PermissionContextHolder 类，<br>
 * 该类使用 RequestContextHolder 在单个 HTTP 请求的范围内存储和检索权限上下文。它允许您设置和获取与当前请求关联的权限
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:40
 */
public class PermissionContextHolder {
    private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

    public static void setContext(String permission) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getContext() {
        Object value = RequestContextHolder.currentRequestAttributes().getAttribute(PERMISSION_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }
}
