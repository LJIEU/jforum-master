package com.liu.core.excption.cache;

import com.liu.core.excption.BaseException;

import java.io.Serial;
import java.util.Locale;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/09 20:50
 */
public class CacheException extends BaseException {
    @Serial
    private static final long serialVersionUID = 6160839912579417472L;


    public CacheException(String code, Object[] args) {
        super("cache", code, args, null, Locale.getDefault().getLanguage());
    }
}
