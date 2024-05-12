package com.liu.db.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * Description: 登录参数
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 20:24
 */
@Schema(name = "登录参数")
public class LoginBody {

    /**
     * 用户名
     */
    @DecimalMin(value = "3")
    @DecimalMax(value = "20")
    private String username;

    /**
     * 密码
     */
    @DecimalMin(value = "6")
    @DecimalMax(value = "20")
    private String password;

    /**
     * 验证码
     */
    private String captchaCode;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 滑块验证码
     */
    private Boolean slider = false;


    public Boolean getSlider() {
        return slider;
    }

    public void setSlider(Boolean slider) {
        this.slider = slider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
