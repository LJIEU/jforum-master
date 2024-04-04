package com.liu.core.constant;

/**
 * Description: 返回状态码
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:42
 */
public interface HttpStatus {

    /**
     * 操作成功
     */
    int SUCCESS = 200;

    /**
     * 重定向
     */
    int SEE_OTHER = 303;

    /**
     * 参数列表错误
     */
    int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    int UNAUTHORIZED = 401;

    /**
     * 资源 服务 未找到
     */
    int NOT_MODIFIED = 404;

    /**
     * 服务器异常
     */
    int SYSTEM_ERROR = 500;
}
