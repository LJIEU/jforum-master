package com.liu.core.constant;

/**
 * Description: 用户常量
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 21:41
 */
public interface UserConstants {
    /**
     * 密码最小长度 最大长度
     */
    int PASSWORD_MIN_LENGTH = 6;
    int PASSWORD_MAX_LENGTH = 20;
    /**
     * 用户名最小长度 最大长度
     */
    int USERNAME_MIN_LENGTH = 3;
    int USERNAME_MAX_LENGTH = 20;
    /**
     * 不是唯一的 不可以进行注册
     */
    boolean NOT_UNIQUE = Boolean.FALSE;
    /**
     * 是唯一的 可以进行注册
     */
    boolean UNIQUE = Boolean.TRUE;

    /**
     * 普通用户标识
     */
    String USER_TYPE = "01";
    /**
     * 管理员用户标识
     */
    String ADMIN_USER_TYPE = "00";
}
