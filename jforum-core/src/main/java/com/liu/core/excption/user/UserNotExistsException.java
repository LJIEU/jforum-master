package com.liu.core.excption.user;

import java.io.Serial;

/**
 * Description: 用户不存在 异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:27
 */
public class UserNotExistsException extends UserException{
    @Serial
    private static final long serialVersionUID = -6232283021123743043L;

    public UserNotExistsException() {
        super("user.not.exist", null);
    }
}
