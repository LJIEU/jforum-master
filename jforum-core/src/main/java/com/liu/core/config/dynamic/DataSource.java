package com.liu.core.config.dynamic;

import com.liu.core.constant.enume.DataSourceType;

import java.lang.annotation.*;

/**
 * Description: 多数据源注解
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 15:08
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     * 数据源名称
     */
    DataSourceType value() default DataSourceType.MASTER;
}
