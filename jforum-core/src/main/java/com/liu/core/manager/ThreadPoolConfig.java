package com.liu.core.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * Description: 线程池配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:21
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {
    // 核心线程池大小
    private final int corePoolSize = 50;

    // 最大可创建的线程数
    private final int maxPoolSize = 200;

    // 队列最大长度
    private final int queueCapacity = 1000;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private final int keepAliveSeconds = 300;

    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                // 打印线程异常信息
                if (t == null && r instanceof Future<?>) {
                    try {
                        Future<?> future = (Future<?>) r;
                        if (future.isDone()) {
                            future.get();
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        t = e;
                        Thread.currentThread().interrupt();
                    }
                }
                if (t != null) {
                    log.error(t.getMessage(), t);
                }
            }
        };
    }
}
