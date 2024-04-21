package com.liu.core.excption;

import com.liu.core.utils.MessageUtils;

import java.io.Serial;

/**
 * Description: 基本异常信息
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:28
 */
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1503656423434203463L;
    /**
     * 所属模块
     */
    private final String module;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误码对应的参数
     */
    private final Object[] args;

    /**
     * 错误消息
     */
    private final String defaultMessage;

    /**
     * 错误消息
     */
    private final String language;


    public BaseException(String module, String code, Object[] args, String defaultMessage, String language) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
        this.language = language;
    }

    public BaseException(String code, Object[] args, String language) {
        this(null, code, args, null, language);
    }


    @Override
    public String getMessage() {
        String message = null;
        if (code != null) {
            message = MessageUtils.message(code, args, language);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
