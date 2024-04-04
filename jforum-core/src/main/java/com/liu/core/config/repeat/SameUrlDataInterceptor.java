package com.liu.core.config.repeat;

import com.liu.core.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description: 防止重复提交URL
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 13:59
 */
@Slf4j
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
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
