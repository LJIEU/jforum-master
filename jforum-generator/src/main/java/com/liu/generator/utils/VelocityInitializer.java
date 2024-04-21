package com.liu.generator.utils;

import com.liu.core.constant.Constants;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * Description: 模板初始化
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 21:17
 */
public class VelocityInitializer {

    /**
     * 初始化
     */
    public static void initVelocity() {
        try {
            Properties properties = new Properties();
//        ClasspathResourceLoader
            // 加载 vm 文件
            properties.setProperty("resource.loader.file.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 设置字符集
            properties.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
            // 初始化 Velocity 引擎 指定配置
            Velocity.init(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
