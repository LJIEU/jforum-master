package com.liu.core.manager;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 杰
 * @version 1.0
 * @description 确保程序退出时关闭所有线程
 * @since 2024/02/20 16:31
 */
@Component
public class ShutdownManager {
    private static final Logger log = LoggerFactory.getLogger(ShutdownManager.class);


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
