package com.liu.security.component;

import com.alibaba.fastjson2.JSON;
import com.liu.core.constant.HttpStatus;
import com.liu.core.result.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 21:21
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = String.format("请求访问: %s --- 没有权限,无法访问系统资源...", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(
                R.fail(HttpStatus.UNAUTHORIZED, message)));
        response.getWriter().flush();// 刷新
    }
}
