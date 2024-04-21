package com.liu.core.excption.user;

import java.io.Serial;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 20:46
 */
public class UserPasswordNotMatchException extends UserException {
    @Serial
    private static final long serialVersionUID = -5745201034303323569L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
