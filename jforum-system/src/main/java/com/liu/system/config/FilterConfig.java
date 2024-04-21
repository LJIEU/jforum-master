package com.liu.system.config;

import com.liu.camunda.config.CamundaSecurityFilter;
import com.liu.core.fiter.RepeatableFilter;
import com.liu.core.fiter.XssFilter;
import com.liu.security.component.JwtAuthenticationTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 17:17
 */
@Configuration
public class FilterConfig {

    /**
     * XSS 拦截器 数字越小 优先级越高 数字相同看排序
     */
    private final static Integer XSS = Integer.MIN_VALUE;
    /**
     * 不可重复提交 拦截器
     */
    private final static Integer REPEATABLE = 1;
    /**
     * JWT 拦截器
     */
    private final static Integer JWT_AUTH = 0;
    /**
     * Camunda 拦截器
     */
    private final static Integer CAMUNDA_AUTH = -10;

    /**
     * 配置跨站攻击过滤器
     */
    @Bean

    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
        // 注册过滤器
        FilterRegistrationBean<XssFilter> filterRegistration =
                new FilterRegistrationBean<>(new XssFilter());
        // 拦截所有
        filterRegistration.addUrlPatterns("/**");
        filterRegistration.setOrder(1);

        /*以下不进行拦截过滤*/
        Map<String, String> initParameters = new HashMap<>(2);
        String excludes = getExcludes();
        initParameters.put("ignorePath", excludes);
        initParameters.put("isIncludeRichText", "true");
        filterRegistration.setInitParameters(initParameters);
        filterRegistration.addUrlPatterns("/**");
        filterRegistration.setName("xssFilter");
        // 优先级更高
        filterRegistration.setOrder(XSS);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean<RepeatableFilter> repeatableFilterRegistrationBean() {
        FilterRegistrationBean<RepeatableFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RepeatableFilter());
        registrationBean.addUrlPatterns("/**");
        registrationBean.setName("repeatableFilter");
        registrationBean.setOrder(REPEATABLE);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationTokenFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<JwtAuthenticationTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationTokenFilter());
        registrationBean.addUrlPatterns("/**");
        registrationBean.setName("jwtAuthenticationToken");
        registrationBean.setOrder(JWT_AUTH);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CamundaSecurityFilter> camundaSecurityRegistrationBean() {
        FilterRegistrationBean<CamundaSecurityFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CamundaSecurityFilter());
        registrationBean.addUrlPatterns("/camunda/**", "/engine-res/**");
        registrationBean.setName("camundaAuthentication");
        registrationBean.setOrder(CAMUNDA_AUTH);
        return registrationBean;
    }


    /**
     * 不需要 XSS 过滤的路径
     */
    private String getExcludes() {
        return new StringJoiner(",")
                .add("/favicon.ico")
                .add("/doc.html")
                .add("/swagger-ui.html")
                .add("/csrf")
                .add("/webjars/**")
                .add("/v3/**")
                .add("/swagger-resources/**")
                .add("/resources/**")
                .add("/static/**")
                .add("/public/**")
                .add("/classpath:*")
                .add("/actuator/**")
                .add("/druid/**")
                .add("**.html")
                .add("/camunda/**")
                .add("/engine-res/**")
                .toString();
    }

}
