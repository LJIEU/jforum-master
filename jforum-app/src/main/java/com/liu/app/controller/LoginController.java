package com.liu.app.controller;

import com.liu.app.service.LoginService;
import com.liu.core.model.LoginUser;
import com.liu.core.result.R;
import com.liu.core.utils.SpringUtils;
import com.liu.db.vo.LoginBody;
import com.liu.security.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 13:57
 */
@Tag(name = "登录模块")
@RequestMapping("/app/api")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCaptchaCode(), loginBody.getUuid(), loginBody.getSlider());
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        return R.success(map);
    }


    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<String> logout() {
        return R.success();
    }

    @Operation(summary = "验证")
    @PostMapping("/verify")
    public R<String> verify(HttpServletRequest request) {
        LoginUser user = SpringUtils.getBean(JwtTokenService.class).getLoginUser(request);
        if (user == null) {
            return R.fail(401, "用户未登录");
        }
        return R.success();
    }

}