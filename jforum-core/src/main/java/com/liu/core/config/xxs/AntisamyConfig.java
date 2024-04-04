package com.liu.core.config.xxs;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: Xss配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 16:08
 */
@Configuration
public class AntisamyConfig {

    /**
     * 反序列化处理器
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2() {
        return builder -> builder
                .deserializerByType(String.class, new XssStringJsonDeserializer());
    }

}
