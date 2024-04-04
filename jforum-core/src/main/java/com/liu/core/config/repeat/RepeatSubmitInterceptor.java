package com.liu.core.config.repeat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * Description: 拦截器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 13:56
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    // SpringBoot3.0 弃用了 JavaEE 改用了 Jakarta EE 文章:https://zhuanlan.zhihu.com/p/646540007

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            /*获取注解*/
            Method method = handlerMethod.getMethod();
            RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
            if (repeatSubmit != null) {
                if (this.isRepeatSubmit(request, repeatSubmit)) {
                    response.setContentType("text/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(repeatSubmit.message());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否是重复提交
     *
     * @param request      请求信息
     * @param repeatSubmit 防止重复提交信息
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit);
}

