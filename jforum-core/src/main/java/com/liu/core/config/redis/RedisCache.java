package com.liu.core.config.redis;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.liu.core.manager.AsyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

    /**
     * 设置值  但是缓存是逻辑过期
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    过期类型 毫秒、秒....
     * @param <T>
     */
    public <T> void setCacheObjectLogicExpired(String key, T value, Long timeout, TimeUnit unit) {
        // 逻辑数据载体
        CacheData cacheData = new CacheData();
        cacheData.setObject(value);
        cacheData.setExpiredTime(LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        // 因为设置的是逻辑过期 所以不设置TTL
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(cacheData));

    }

    /**
     * 逻辑过期后 返回过期数据 开启异步线程进行更新数据
     *
     * @param key      键
     * @param id       数据查询ID
     * @param type     返回值
     * @param fallback 方法  ==》
     * @param timeOut  过期时间
     * @param unit     时间类型：毫秒、秒
     * @param <T>      ID类型
     * @param <D>      返回数据类型
     * @return
     */
    public <T, D> D getCacheObjectBySecurity(String key, T id, Class<D> type, Function<T, D> fallback,
                                             Long timeOut, TimeUnit unit) {
        String json = null;
        try {
            json = (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {

        }
        // 判断是否为null
        if (json == null || StrUtil.isEmpty(json)) {
            return null;
//            throw new CacheIsNullException();
        }
        // 获取值
        CacheData cacheData = JSONUtil.toBean(json, CacheData.class);
        LocalDateTime expiredTime = cacheData.getExpiredTime();
        D object = JSONUtil.toBean((JSONObject) cacheData.getObject(), type);
        if (expiredTime.isAfter(LocalDateTime.now())) {
            // 未过期直接返回数据
            return object;
        }

        String lockKey = "redis:lock:" + key;
        // 异步开启 进行 重新缓存数据
        // 1.获取锁 只能有一个线程去实现 数据的缓存
        boolean lock = tryLock(lockKey);
        if (lock) {
            try {
                // 2.执行异步任务
                AsyncManager.manager().execute(new TimerTask() {
                    @Override
                    public void run() {
                        // 查询数据库
                        D newData = fallback.apply(id);
                        // 将数据缓存
                        setCacheObjectLogicExpired(key, newData, timeOut, unit);
                    }
                });
            } catch (Exception e) {
                return null;
            } finally {
                // 释放锁
                unLock(lockKey);
            }
        }

        // 过期了也返回  过期的结果
        return object;
    }

    private final Long TIME = 1712719076L;

    /**
     * 生成 唯一ID
     *
     * @param key 键
     * @return 返回 Long 类型的ID
     */
    public Long uniqueId(String key) {
        // LocalDateTime.now() 获取当前的日期和时间但是不包含时区信息
        // toEpochSecond(ZoneOffset.UTC) 是将时间转换成从 Epoch[1970年1月1日 00:00:00 UTC] 开始的秒数
        long nowTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        // 获取时间戳  当前时间 - 2024/4/10 号的时间戳
        long startTime = nowTime - TIME;
//        System.out.println(Thread.currentThread().getName() + ":" + startTime);
        // 设置 Key 前缀
        String preKey = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 获取 计数
        Long count = redisTemplate.opsForValue().increment(preKey + ":" + key);

        // 将时间戳向做移动32位[给计数器空出位置前面32为时间戳startTime]
        // 计算机是 62bit 的由于 时间戳最高位为0正数 ==》 时间戳 31 位
        /**
         *                 31                       |        计数器 32位 ===> 4,294,967,296  ==》 一天40亿的数据ID 完全够用了
         * 0 000 0000 0000 0000 1000 0011 1001 0110 | 0000 0000 0000 0000 0000 0000 1101 0001
         * 0 000 0000 0000 0000 1000 0011 1001 0110 | 0000 0000 0000 0000 0000 0000 0000 0000  =》 时间戳 左移32位后
         * 0 000 0000 0000 0000 0000 0000 0000 0000 | 0000 0000 0000 0000 0000 0000 1101 0010  =》 count
         * 0 000 0000 0000 0000 1000 0011 1001 0110 | 0000 0000 0000 0000 0000 0000 1101 0010  =》 二者进行 或运算的结果
         *
         * 但是在 2^31 - 1 + TIME =》 3,860,202,723 秒后 即 ==》 2092-4-28 后就会开始出现重复的ID ==》 68年 多一些【有闰年的因素】
         */
        return startTime << 32 | count;
    }


    private boolean tryLock(String key) {
        // 添加锁  如果不存在则添加   添加成功返回true
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "lock", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key) {
        redisTemplate.delete(key);
    }
}
