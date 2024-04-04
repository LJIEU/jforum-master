package com.liu.core.config.repeat;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.CacheConstants;
import com.liu.core.excption.RepeatSubmitException;
import com.liu.core.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description: 防止重复提交切面
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 13:53
 */
@Slf4j
@Aspect
@Component
public class RepeatSubmitAspect {
    private final String REPEAT_PARAMS = "repeat_params";
    private final String REPEAT_TIME = "repeat_time";

    @Value("${token.header}")
    private String header;

    @Autowired
    private RedisCache redisCache;

    @Pointcut("@annotation(repeatSubmit)")
    public void pointCut(RepeatSubmit repeatSubmit) {

    }

    @SuppressWarnings("unchecked")
    @Around(value = "pointCut(repeatSubmit)", argNames = "point,repeatSubmit")
    public Object around(ProceedingJoinPoint point, RepeatSubmit repeatSubmit) throws Throwable {
        /*获取注解的属性*/
        // 间隔时间 默认 5000ms
        int interval = repeatSubmit.interval();
        String message = repeatSubmit.message();
        /*Body数据处理*/
        String body = JSONUtil.toJsonStr(point.getArgs());
        log.warn("解析Body数据: {}", body);

        /*如果Body为空 那就获取URL中的数据*/
        HttpServletRequest request = ServletUtils.getRequest();
        if (StringUtils.isEmpty(body)) {
            body = JSON.toJSONString(request.getParameterMap());
        }

        HashMap<String, Object> nowDataMap = new HashMap<>(2);
        nowDataMap.put(REPEAT_PARAMS, body);
        // 当前存入时间
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());
        // 将这个数据存入缓存中
        String uri = request.getRequestURI();
        String submitKey = StringUtils.trimToEmpty(request.getHeader(header));
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + ":"
                + uri + ":" + submitKey;
        // 先获取一下 查看是否存在 重复提交问题
        HashMap<String, Object> cacheObject = redisCache.getCacheObject(cacheRepeatKey);
        if (cacheObject != null) {
            if (cacheObject.containsKey(uri)) {
                Map<String, Object> preDataMap = (Map<String, Object>) cacheObject.get(uri);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, interval)) {
                    throw new RepeatSubmitException(message, 200);
                }
            }
        }
        // 否则存入缓存
        HashMap<String, Object> cacheMap = new HashMap<>(1);
        cacheMap.put(uri, nowDataMap);
        redisCache.setCacheObject(cacheRepeatKey, cacheMap, interval, TimeUnit.MILLISECONDS);
        // 如果没有什么问题让程序进行向下执行
        return point.proceed();

    }

    /**
     * 判断间隔时间
     *
     * @param nowDataMap 当前数据
     * @param preDataMap 缓存中的数据
     * @param interval   间隔时间
     */
    private boolean compareTime(HashMap<String, Object> nowDataMap, Map<String, Object> preDataMap, int interval) {
        Long nowTime = (Long) nowDataMap.get(REPEAT_TIME);
        Long preTime = (Long) preDataMap.get(REPEAT_TIME);
        return (nowTime - preTime) < interval;
    }

    /**
     * 判断参数是否相同
     *
     * @param nowDataMap 当前数据
     * @param preDataMap 缓存中的数据
     */
    private boolean compareParams(HashMap<String, Object> nowDataMap, Map<String, Object> preDataMap) {
        String nowParams = (String) nowDataMap.get(REPEAT_PARAMS);
        String preParams = (String) preDataMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }
}