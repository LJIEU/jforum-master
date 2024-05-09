package com.liu.core.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.liu.core.annotation.Log;
import com.liu.core.constant.Constants;
import com.liu.core.fiter.PropertyPreExcludeFilter;
import com.liu.core.model.BaseOperateLog;
import com.liu.core.utils.IpUtils;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.ServletUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * Description: 操作日志切面
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:58
 */
public class BaseLogAspect {
    private static final Logger log = LoggerFactory.getLogger(BaseLogAspect.class);

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> THREAD_LOCAL = new NamedThreadLocal<>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(Log controllerLog) {
        THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理请求完成后执行
     */
    @SneakyThrows
    @AfterReturning(value = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    public BaseOperateLog handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        BaseOperateLog baseOperateLog = new BaseOperateLog();
        try {
            // 请求地址
            HttpServletRequest request = ServletUtils.getRequest();
            String ip = IpUtils.getIpAddress();
            String uri = StringUtils.substring(request.getRequestURI(), 0, 255);
            // 请求方式
            String requestMethod = request.getMethod();
            String errMessage = "";
            // 异常信息
            if (e != null) {
                errMessage = StringUtils.substring(e.getMessage(), 0, 2000);
            }
            // 类名称 --> 方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            // 获取类上的 @Tag 注解中的 name 值 这个就是模块名称
            String moduleName = joinPoint.getTarget().getClass().getAnnotation(Tag.class).name();
            if (StringUtils.isNotBlank(moduleName)) {
                baseOperateLog.setModuleName(moduleName);
            }
            // 获取注解上的参数
            int action = controllerLog.businessType().ordinal();
            String describe = controllerLog.describe();
            int ordinal = controllerLog.operatorType().ordinal();
            if (StringUtils.isNotBlank(moduleName)) {
                baseOperateLog.setModuleName(moduleName);
            }
            // 获取 请求体中的数据
            String requestData = "";
            if (controllerLog.isSaveRequestData()) {
                // 获取参数信息
                Map<?, ?> paramMap = ServletUtils.getParamMap(request);
                // 如果是POST获取PUT请求信息
                if (CollUtil.isEmpty(paramMap)
                        && (HttpMethod.PUT.name().equals(requestMethod) ||
                        HttpMethod.POST.name().equals(requestMethod)
                )) {
                    requestData = StringUtils.substring(argsArrayToString(joinPoint.getArgs()
                            , controllerLog.excludeParamNames()), 0, 2000);

                } else {
                    // 否则直接包装返回即可 ?id=1&name=2
                    requestData = StringUtils.substring(JSON.toJSONString(paramMap,
                            excludePropertyPreFilter(controllerLog.excludeParamNames())), 0, 2000);
                }
            }
            // 是否想要保存 响应的数据
            String responseData = "";
            if (controllerLog.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
                responseData = StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000);
            }

            // 消耗时间
            long costTime = System.currentTimeMillis() - THREAD_LOCAL.get();

/*            // 打印信息 ================> 也可以收集这些信息 后期存入数据表中
            log.info("""

                            IP:{}\t::[{}]::URI:{}
                            ERR:{}
                            类名:{} --> 方法名:{}
                             ====== 注解参数 ======
                             业务类型:{} \t 描述:{} \t 操作人员类别:{}
                             ====== 请求参数 ======
                             {}
                             ====== 响应结果 ======
                             {}
                             ----消耗时间----》{}ms
                            """, ip, requestMethod, uri, errMessage,
                    className, methodName,
                    action, describe, ordinal,
                    requestData, responseData,
                    costTime);*/
            baseOperateLog.setUsername(SecurityUtils.currentUsername(request));
            baseOperateLog.setIp(ip);
            baseOperateLog.setRequestMethod(requestMethod);
            baseOperateLog.setUri(uri);
            baseOperateLog.setErrMessage(errMessage);
            baseOperateLog.setClassName(className);
            baseOperateLog.setMethodName(methodName);
            baseOperateLog.setAction(action);
            baseOperateLog.setDescribe(describe);
            baseOperateLog.setOrdinal(ordinal);
            baseOperateLog.setRequestData(requestData);
            baseOperateLog.setResponseData(responseData);
            baseOperateLog.setCostTime(costTime);
        } catch (Exception ex) {
            log.error("异常信息:{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            // 删除时间线程
            THREAD_LOCAL.remove();
        }
        return baseOperateLog;
    }


    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }


    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter()
                .addExcludes(ArrayUtils
                        .addAll(Constants.EXCLUDE_PROPERTIES, excludeParamNames));
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
