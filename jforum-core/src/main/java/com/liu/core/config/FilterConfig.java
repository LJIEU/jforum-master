package com.liu.core.config;

import com.liu.core.config.repeat.RepeatableFilter;
import com.liu.core.config.xxs.XssFilter;
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
        String excludes = new StringJoiner(",")
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
                .toString();
        initParameters.put("excludes", excludes);
        initParameters.put("isIncludeRichText", "true");
        filterRegistration.setInitParameters(initParameters);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("xssFilter");
        // 优先级更高
        filterRegistration.setOrder(-1);
        return filterRegistration;
    }


    @Bean
    public FilterRegistrationBean<RepeatableFilter> repeatableFilterRegistrationBean() {
        FilterRegistrationBean<RepeatableFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RepeatableFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("repeatableFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
