package com.liu.core.excption.user;

import java.io.Serial;

/**
 * Description: 缓存异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 21:45
 */
public class CaptchaExpireException extends UserException {
    @Serial
    private static final long serialVersionUID = 8210309634215282190L;

    public CaptchaExpireException() {
        super("user.captcha.expire" , null);
    }
}
