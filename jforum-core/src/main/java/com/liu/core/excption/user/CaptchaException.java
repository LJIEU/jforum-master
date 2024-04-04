package com.liu.core.excption.user;

import java.io.Serial;

/**
 * Description: 缓存异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 21:47
 */
public class CaptchaException extends UserException {
    @Serial
    private static final long serialVersionUID = -1746421475877600423L;

    public CaptchaException() {
        super("user.captcha.error", null);
    }
}
