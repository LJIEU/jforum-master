package com.liu.core.excption;

import java.io.Serial;

/**
 * Description: 业务异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 15:06
 */
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8596473089053029823L;
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细
     */
    private String detailMessage;

    public ServiceException() {

    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
