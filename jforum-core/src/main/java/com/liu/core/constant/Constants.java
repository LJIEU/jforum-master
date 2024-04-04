package com.liu.core.constant;

/**
 * Description: 常量
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 11:07
 */
public interface Constants {
    /**
     * 线程池等待关闭时间 120s
     */
    int AWAIT_TIME = 120;

    /**
     * 线程执行任务 操作延迟 10 ms
     */
    int DELAY_TIME = 10;

    /**
     * BaseLogAspect 中排除敏感属性字段
     */
    String[] EXCLUDE_PROPERTIES = {"password"};


    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * 令牌参数1
     */
    String LOGIN_USER_KEY = "login_user_key";

    /**
     * 令牌参数2
     */
    String LOGIN_USER_NAME = "username";

    /**
     * 令牌参数3
     */
    String LOGIN_OS = "os";

    /**
     * 按需加上需要支持自动类型的类名前缀 范围越小越安全
     */
    String[] JSON_WHITELIST_STR = {"org.springframework", "com.liu"};

    /**
     * 所有权限标识
     */
    String ALL_PERMISSION = "*:*:*";

    /**
     * 登录失败 状态 1
     */
    Integer LOGIN_FAIL_STATUS = 1;

    /**
     * 登录成功 状态 1
     */
    Integer LOGIN_SUCCESS_STATUS = 0;

    /**
     * 验证码过期时间 2 分钟
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 失败
     */
    String LOGIN_FAIL = "error";
    /**
     * 成功
     */
    String LOGIN_SUCCESS = "success";
    /**
     * 注册
     */
    String REGISTER = "注册";

    String LOGOUT = "退出";
    /**
     * 登录成功
     */
    String SUCCESS = "0";
    /**
     * 登录失败
     */
    String FAIL = "1";
}
