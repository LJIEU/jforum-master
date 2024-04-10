package com.liu.core.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 杰
 * @version 1.0
 * @description Redis配置
 * @since 2024/02/18 17:48
 */
@SuppressWarnings("all")
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurationSelector {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // key 和 hash key 采用 string 序列化
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<Object>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        // value 和 hash value 采用 fastjson
        template.setValueSerializer(fastJson2RedisSerializer);
        template.setHashValueSerializer(fastJson2RedisSerializer);

//        log.info("key 序列化:{}", JSONUtil.toJsonStr(stringRedisSerializer));
//        log.info("value 序列化:{}", JSONUtil.toJsonStr(fastJson2RedisSerializer));
//        log.info("value 序列化:{}", JSONUtil.toJsonStr(template));
/*  无效配置  无法实现 读取 Redis 中的数据
        // 转译
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY
        );
        // JSON 序列化配置
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<Object>(mapper, Object.class);

        // String 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 和 hash key 采用 string 序列化
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // value 和 hash value 采用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
*/

        // 使配置生效
        template.afterPropertiesSet();
        return template;
    }


}
