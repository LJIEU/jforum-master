package com.liu.core.config.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 动态数据源切换处理
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 15:12
 */
public class DynamicDataSourceContextHolder {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);


    /**
     * ThreadLocal 为每一个使用该变量的线程提供独立的变量副本
     * 每个线程都可以独立地改变自己的副本而不影响其他线程所对应的副本
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源变量
     */
    public static void setDataSourceType(String dataSourceType) {
        log.info("切换到{}数据源", dataSourceType);
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 获取数据源变量
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}
