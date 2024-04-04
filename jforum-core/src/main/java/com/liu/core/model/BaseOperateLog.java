package com.liu.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description: 操作日志实体类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:12
 */
@Getter
@Setter
public class BaseOperateLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 8437340490036790603L;
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
}
