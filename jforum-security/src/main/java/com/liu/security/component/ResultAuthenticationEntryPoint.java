package com.liu.security.component;

import com.alibaba.fastjson2.JSON;
import com.liu.core.constant.HttpStatus;
import com.liu.core.result.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 21:19
 */
@Component
public class ResultAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Serial
    private static final long serialVersionUID = 8941051271765260066L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = String.format("请求访问: %s --- Token失效,无法访问系统资源...", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(
                JSON.toJSONString(
                        R.fail(HttpStatus.UNAUTHORIZED, message)));
        // 重定向到登录页  【可以由前端去控制跳转】
//        response.sendRedirect(request.getContextPath() + "/login");
    }
}
