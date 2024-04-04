package com.liu.core.excption;

import java.io.Serial;

/**
 * Description: 禁止重复提交
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 19:42
 */
public class RepeatSubmitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4911468379164108569L;


    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    public RepeatSubmitException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public RepeatSubmitException(String message) {
        this.message = message;
        this.code = 200;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
