package com.liu.security.config;

import com.liu.security.component.JwtAuthenticationTokenFilter;
import com.liu.security.component.RestAccessDeniedHandler;
import com.liu.security.component.ResultAuthenticationEntryPoint;
import com.liu.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:12
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private ResultAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 拒绝访问后处理类
     */
    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }


    /**
     * 身份校验机制、身份验证提供程序
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置用户相关信息，可以从数据库中读取、或者缓存、或者配置文件
        authProvider.setUserDetailsService(userDetailsService);
        // 设置加密机制，用于对用户进行身份验证
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    /**
     * 基于用户名和密码或使用用户名和密码进行身份验证
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * 同源策略配置
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何源
        corsConfiguration.addAllowedOriginPattern("*");
        // 允许任何方法
        corsConfiguration.addAllowedMethod("*");
        // 允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 允许任何证书 cookies
        corsConfiguration.setAllowCredentials(true);
        // 预检请求的缓存时间 s
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        // CSRF禁用[防止跨站请求伪造攻击] 不使用 session
                        .csrf(AbstractHttpConfigurer::disable)

                        // 基于Token 不需要 session
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        // 过滤请求
                        .authorizeHttpRequests(auth -> auth
                                // 匿名访问
                                .requestMatchers("/test/index","/**",
                                        "/test/test", "/test/test2", "/test/test3", "/test/test4", "/test/test5",
                                        "/test/test8", "/test/test9/*","/gen/**",
                                        "/captcha/**", "*/login", "*/register" ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/",
                                        "/**.htm", "/**.html", "/**.css", "/**.js").anonymous()
                                .requestMatchers("/swagger-ui/**",
                                        "/swagger-resources/**", "/webjars/**", "/*/api-docs/**").anonymous()
                                .requestMatchers("/druid/**").anonymous()
                                // 除了上面的请求 其他所有请求都需要鉴权认证
                                .anyRequest().authenticated())
                        // 退出
                        .logout(Customizer.withDefaults())
                        // 异常处理器
                        .exceptionHandling(exceptionHandle -> exceptionHandle
                                .accessDeniedHandler(restAccessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint))
                        // JWT过滤器
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtAuthenticationTokenFilter(), ExceptionTranslationFilter.class)
                        .httpBasic(Customizer.withDefaults())
                        // 禁用缓存
                        .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                        // 允许跨域
                        .cors(cors -> cors
                                .configurationSource(configurationSource()))
                        .build();
    }
}
