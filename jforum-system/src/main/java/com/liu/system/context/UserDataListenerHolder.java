package com.liu.system.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Description: 返回 保存结果信息
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/15 23:07
 */
public class UserDataListenerHolder {
    private static final String USER_DATA_CONTEXT_ATTRIBUTES = "USER_DATA_LISTENER";

    public static void setContext(String userData) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute(USER_DATA_CONTEXT_ATTRIBUTES, userData, RequestAttributes.SCOPE_REQUEST);
    }

    public static String getContext() {
        Object value = RequestContextHolder.currentRequestAttributes().getAttribute(USER_DATA_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

}
