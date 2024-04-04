package com.liu.core.config.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static com.google.code.kaptcha.Constants.*;

/**
 * Description: 验证码配置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 18:40
 */
@Configuration
public class CaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha defaultKaptcha() {
        // 字符验证码配置
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 配置验证码图片
        // 边框 默认true
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 验证码文本验证 默认黑色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 宽度 默认 200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 高度 默认 50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 字符大小 默认 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        // 字符串长度 默认 5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // 字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        /*
        图片样式
        水纹 WaterRipple
        鱼眼 FishEyeGimpy
        阴影 ShadowGimpy
        */
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha defaultKaptchaToMath() {
        // 字符验证码配置
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 配置验证码图片
        // 边框 默认yes  不能设置"true"  Value must be either yes or no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // 验证码文本验证 默认黑色
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "105,179,90");
        // 宽度 默认 200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // 高度 默认 50
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 字符大小 默认 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // 验证码文本生成器
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "com.liu.core.config.captcha.KaptchaTextCreator");
        // 字符间距 默认 2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // 字符串长度 默认 5  [生成文本最长 10*10=?@结果值  ==> 截取6个字符 10*10=?]
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "7");
        // 字体样式
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // 验证码噪点颜色 默认 Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // 干扰实现类
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
