package com.liu.core.config.repeat.impl;

import com.liu.core.config.repeat.RepeatSubmit;
import com.liu.core.config.repeat.RepeatSubmitInterceptor;
import com.liu.core.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: 防止重复提交URL
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 13:59
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SameUrlDataInterceptor.class);

    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) {
        // 判断是否是重复提交URL
        String nowParams = "";
        // 2024/3/4/22:03  RepeatedlyRequestWrapper 是继承了 HttpServletRequestWrapper -> 实现了 HttpServletRequest
        // 可是 request 和 RepeatedlyRequestWrapper 不同...
        if (request != null) {
            nowParams = HttpUtils.getBodyString(request);
            log.warn(nowParams);
        }
        return false;
    }
}
