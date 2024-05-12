package com.liu.app.service;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 15:57
 */
public interface LoginService {
    /**
     * 登录
     *
     * @param username    账号
     * @param password    密码
     * @param captchaCode 验证码
     * @param uuid        验证码UUID
     * @param slider
     * @return 登录成功返回token
     */
    String login(String username, String password, String captchaCode, String uuid, Boolean slider);
}
