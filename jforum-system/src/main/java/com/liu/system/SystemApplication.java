package com.liu.system;

import com.liu.generator.GenProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description: 启动类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:12
 */
@SuppressWarnings("all")
@MapperScan({"com.liu.**.mapper"})
@ComponentScan({"com.liu.core.*",
        "com.liu.generator.*",
        "com.liu.camunda.*",
        "com.liu.security.*"})
@SpringBootApplication(exclude = {/*SecurityAutoConfiguration.class*/})
@Slf4j
public class SystemApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(SystemApplication.class, args);
        Environment env = application.getEnvironment();
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

/*    @Bean
    public BaseLogAspect logAspect() {
        return new BaseLogAspect();
    }*/

    @Bean
    public GenProperties genProperties() {
        return new GenProperties();
    }
}
