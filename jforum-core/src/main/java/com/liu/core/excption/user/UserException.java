package com.liu.core.excption.user;

import com.liu.core.excption.BaseException;

import java.io.Serial;
import java.util.Locale;

/**
 * Description: 关于用户的一些异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:28
 */
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = 2115378795881628059L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null, Locale.getDefault().getLanguage());
    }
}
