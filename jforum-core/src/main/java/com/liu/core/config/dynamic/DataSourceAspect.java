package com.liu.core.config.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description: 数据源切换切面
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 15:11
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    @Pointcut("@annotation(DataSource)")
    public void doPointCut() {

    }

    @Around("doPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);
        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }


    /**
     * 获取需要切换的数据源 [获取参数]
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }
        // 否则返回默认设置类型 DataSource.class 注解类类型
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
