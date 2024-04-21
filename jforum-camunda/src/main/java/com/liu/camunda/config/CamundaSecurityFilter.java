package com.liu.camunda.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * Description:
 * Camunda 的 安全配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 12:27
 */
public class CamundaSecurityFilter implements Filter {
    public static final Logger log = LoggerFactory.getLogger(CamundaSecurityFilter.class);

    @Bean
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> processEngineAuthenticationFilter() {
        FilterRegistrationBean<ProcessEngineAuthenticationFilter> processEngineAuthenticationFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        processEngineAuthenticationFilterFilterRegistrationBean.setName("camunda-auth");
        processEngineAuthenticationFilterFilterRegistrationBean.setFilter(new ProcessEngineAuthenticationFilter());
        processEngineAuthenticationFilterFilterRegistrationBean.addInitParameter("authentication-provider", "com.liu.camunda.config.CamundaAuthenticationProvider");
        processEngineAuthenticationFilterFilterRegistrationBean.addUrlPatterns("/camunda/**", "/engine-res/**");
        return processEngineAuthenticationFilterFilterRegistrationBean;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        log.info("camundaFilter 放行:{}", servletRequest.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
