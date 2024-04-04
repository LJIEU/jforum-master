package com.liu.core.service;

import org.springframework.stereotype.Service;

/**
 * Description: 系统配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 9:57
 */
@Service
public abstract class BaseConfigService {
    public abstract boolean selectCaptchaEnabled();
}
