package com.liu.camunda.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationProvider;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.springframework.http.HttpHeaders;

/**
 * Description:  Camunda 登录校验
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 12:35
 */
public class CamundaAuthenticationProvider implements AuthenticationProvider {
    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {
        String authorizationHeader = request.getHeader("CamundaAuth");
        if (authorizationHeader != null) {
            String[] userInfo = authorizationHeader.split(":");
            String username = userInfo[0];
            String password = userInfo[1];
            return this.isAuthenticated(engine, username, password) ?
                    AuthenticationResult.successful(username) : AuthenticationResult.unsuccessful(username);
        }
        return AuthenticationResult.unsuccessful();
    }

    protected boolean isAuthenticated(ProcessEngine engine, String userName, String password) {
        return engine.getIdentityService().checkPassword(userName, password);
    }

    @Override
    public void augmentResponseByAuthenticationChallenge(HttpServletResponse response, ProcessEngine
            engine) {
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"" + engine.getName() + "\"");
    }

}