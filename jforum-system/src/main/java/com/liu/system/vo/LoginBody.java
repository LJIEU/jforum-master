package com.liu.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

/**
 * Description: 登录参数
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 20:24
 */
@Schema(name = "登录参数")
@Data
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
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
