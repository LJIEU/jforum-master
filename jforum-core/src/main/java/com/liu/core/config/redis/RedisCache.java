package com.liu.core.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description: 缓存工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 21:32
 */
@SuppressWarnings("all")
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存基本对象
     *
     * @param key   缓存键值
     * @param value 缓存值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本对象
     *
     * @param key      缓存键值
     * @param value    缓存值
     * @param timeout  时间
     * @param timeUnit 时间粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取缓存的基本对象
     *
     * @param key 键值
     * @return 缓存中的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }


    /**
     * 查看Redis中是否存在值
     *
     * @param keysPrefix 前缀Key
     * @return 完整的Key
     */
    public String existValue(final String keysPrefix) {
        Set<String> keys = redisTemplate.keys(keysPrefix);
        for (String key : keys) {
            if (redisTemplate.hasKey(key)) {
                return key;
            }
        }
        return null;
    }

    /**
     * 删除单个对象
     */
    public boolean delObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     */
    public boolean delObject(final Collection collection) {
        return redisTemplate.delete(collection) > 0;
    }
}
