package com.liu.core.controller;

import com.google.code.kaptcha.Producer;
import com.liu.core.config.redis.RedisCache;
import com.liu.core.constant.CacheConstants;
import com.liu.core.constant.Constants;
import com.liu.core.result.R;
import com.liu.core.service.BaseConfigService;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description: 验证码模块
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 22:23
 */
@RestController
@Tag(name = "验证码接口")
@RequestMapping("/captcha")
public class CaptchaController {

    @Value("${captchaType}")
    private String captchaType;

    @Autowired
    private RedisCache redisCache;

    private final BaseConfigService configService = SpringUtils.getBean(BaseConfigService.class);

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    /**
     * 生成验证码
     */
    @SuppressWarnings("all")
    @GetMapping("/captchaImage")
    public R getCode(HttpServletResponse response) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (!captchaEnabled) {
            return R.success("验证码未开启~");
        }

        // 基本信息设置
        String uuid = UUIDUtils.noLinesFormat();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + ":" + uuid;
        String captchaStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equalsIgnoreCase(captchaType)) {
            // 生成文本为 10*10=?@结果值
            String text = captchaProducerMath.createText();
            // 返回给用户数据 10*10=?
            captchaStr = text.substring(0, text.lastIndexOf("@"));
            // 结果值保留到缓存中 结果值
            code = text.substring(text.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(captchaStr);
        } else if ("char".equalsIgnoreCase(captchaType)) {
            captchaStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(captchaStr);
        }

        // 存入缓存
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        // 转换成流信息写出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String base64Code = "";
        try {
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (IOException e) {
            return R.fail(e.getMessage());
        }
        HashMap<String, String> result = new HashMap<>(2);
        result.put("uuid", uuid);
        result.put("img", "data:image/png;base64," + base64Code);
        return R.success(result);
    }
}
