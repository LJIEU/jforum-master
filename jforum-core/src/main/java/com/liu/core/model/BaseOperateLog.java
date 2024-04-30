package com.liu.core.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description: 操作日志实体类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:12
 */
public class BaseOperateLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 8437340490036790603L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 操作IP地址
     */
    private String ip;
    /**
     * 请求方式 POST|GET|PUT...
     */
    private String requestMethod;
    /**
     * 请求URI
     */
    private String uri;
    /**
     * 错误信息
     */
    private String errMessage;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * BusinessType  业务类型对应下标
     */
    private Integer action;
    /**
     * 描述
     */
    private String describe;
    /**
     * OperatorType 操作人员类别 对应下标
     */
    private Integer ordinal;
    /**
     * 请求参数
     */
    private String requestData;
    /**
     * 返回结果
     */
    private String responseData;
    /**
     * 执行时间
     */
    private Long costTime;

    @Override
    public String toString() {
        // 打印信息 ================> 也可以收集这些信息 后期存入数据表中
        System.out.format("""
                                                    
                        IP:%s\t::[%s]::URI:%s
                        ERR:%s
                        类名:%s --> 方法名:%s
                         ====== 注解参数 ======
                         业务类型:%d \t 描述:%s \t 操作人员类别:%d
                         ====== 请求参数 ======
                         %s
                         ====== 响应结果 ======
                         %s
                         ----消耗时间----》%dms
                        """, ip, requestMethod, uri, errMessage,
                className, methodName,
                action, describe, ordinal,
                requestData, responseData,
                costTime);
        return "打印完成";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }
}
