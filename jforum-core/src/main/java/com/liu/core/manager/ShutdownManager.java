package com.liu.core.manager;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 杰
 * @version 1.0
 * @description 确保程序退出时关闭所有线程
 * @since 2024/02/20 16:31
 */
@Slf4j
@Component
public class ShutdownManager {

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    private void shutdownAsyncManager() {
        try {
            log.info("====关闭后台任务线程池====");
            AsyncManager.manager().shutdown();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
