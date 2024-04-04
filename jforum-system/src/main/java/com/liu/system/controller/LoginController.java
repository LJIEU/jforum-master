package com.liu.system.controller;

import com.liu.core.result.R;
import com.liu.system.service.LoginService;
import com.liu.system.service.SysConfigService;
import com.liu.system.vo.LoginBody;
import com.liu.system.vo.RegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 登录
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 18:54
 */
@RestController
@RequestMapping("/")
@Tag(name = "登录模块")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SysConfigService configService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<String> login(LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        return R.success(token);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public R<String> register(RegisterBody registerBody) {
        if (!("true".equalsIgnoreCase(configService.selectConfigByKey("sys.account.register")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        String message = loginService.register(registerBody);
        return StringUtils.isEmpty(message) ? R.success() : R.fail(message);
    }
}
