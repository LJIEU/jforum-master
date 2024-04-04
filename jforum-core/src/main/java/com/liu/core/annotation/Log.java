package com.liu.core.annotation;

import com.liu.core.constant.enume.BusinessType;
import com.liu.core.constant.enume.OperatorType;

import java.lang.annotation.*;

/**
 * Description: 日志注解
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:45
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 描述
     */
    String describe() default "";

    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};
}
