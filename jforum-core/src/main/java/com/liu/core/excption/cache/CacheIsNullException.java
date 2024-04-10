package com.liu.core.excption.cache;

import java.io.Serial;

/**
 * Description: 缓存为null异常
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/09 20:49
 */
public class CacheIsNullException extends CacheException {

    @Serial
    private static final long serialVersionUID = 4837080284161477792L;

    public CacheIsNullException() {
        super("cache.null", null);
    }
}
