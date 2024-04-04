package com.liu.core.constant;

/**
 * Description: 缓存常量
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 21:30
 */
public interface CacheConstants {

    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY = "login_tokens";

    /**
     * 系统配置 redis key
     */
    String SYS_CONFIG_KEY = "sys_config";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "captcha_code";

    /**
     * 重复提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit";
}
