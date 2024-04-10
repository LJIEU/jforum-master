package com.liu.core.config.redis;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 设置逻辑删除
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/09 20:33
 */
public class CacheData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -5059493429663111691L;
    private T object;
    private LocalDateTime expiredTime;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
}
