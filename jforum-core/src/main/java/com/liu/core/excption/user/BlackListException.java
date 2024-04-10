package com.liu.core.excption.user;

import java.io.Serial;

/**
 * Description: 黑名单
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 21:44
 */
public class BlackListException extends UserException {
    @Serial
    private static final long serialVersionUID = -5043399062076909187L;

    public BlackListException() {
        super("user.login.blocked", null);
    }

}
