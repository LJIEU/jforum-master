package com.liu.core.config.repeat;

import java.lang.annotation.*;

/**
 * Description: 防止重复提交
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 13:52
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 间隔时间(ms) 默认5秒  小于此时间视为重复提交
     */
    int interval() default 5000;

    /**
     * 提示信息
     */
    String message() default "禁止重复提交!稍后重试~";
}
