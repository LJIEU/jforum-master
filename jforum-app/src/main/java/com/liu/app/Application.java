package com.liu.app;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 13:59
 */
@MapperScan({"com.liu.**.mapper"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {
        "com.liu.app.*",
        "com.liu.core.*",
        "com.liu.db.*",
        "com.liu.camunda.*",
        "com.liu.security.*"})
@SpringBootApplication
public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        log.info("""
                                       
                        ----------------------------------------------------------
                        ===================  {} 启动成功 =================
                        \t  External: \thttp://{}:{}
                        \tKnife4j文档: \thttp://{}:{}/doc.html
                        \t  Druid页面: \thttp://{}:{}/druid/  ---> username:{}  password:{}
                        ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                /*External*/
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                /*Knife4j*/
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                /*Druid*/
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("spring.datasource.druid.stat-view-servlet.login-username"),
                env.getProperty("spring.datasource.druid.stat-view-servlet.login-password"));

    }
}
