package com.liu.core.manager;

import com.liu.core.constant.Constants;
import com.liu.core.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 杰
 * @version 1.0
 * @description 异步任务管理器
 * @since 2024/02/15 11:23
 */
public class AsyncManager {
    private static final Logger log = LoggerFactory.getLogger(AsyncManager.class);

    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executorService = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     * 以下属于: 饿汉式单例模式
     * 单例实例在类加载时就被创建并且是私有静态成员变量 在类的内部通过一个公有静态方法返回该单例对象
     * 懒汉式单例模式 单例实例在第一次使用时进行创建 而不是在类加载时就创建。
     */
    private AsyncManager() {
    }

    private static final AsyncManager MANAGER = new AsyncManager();

    public static AsyncManager manager() {
        return MANAGER;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        executorService.schedule(task, Constants.DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        // 如果 线程池不是停止的
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                // 如果等待120s后仍然存在
                if (!executorService.awaitTermination(Constants.AWAIT_TIME, TimeUnit.SECONDS)) {
                    executorService.shutdown();
                    if (!executorService.awaitTermination(Constants.AWAIT_TIME, TimeUnit.SECONDS)) {
                        log.info("线程池未停止~~");
                    }
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

}
