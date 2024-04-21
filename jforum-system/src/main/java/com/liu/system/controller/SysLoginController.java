package com.liu.system.controller;

import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.result.R;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysUser;
import com.liu.system.service.LoginService;
import com.liu.db.service.SysConfigService;
import com.liu.db.service.SysUserService;
import com.liu.db.vo.LoginBody;
import com.liu.db.vo.RegisterBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 登录
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 18:54
 */
@RestController
@RequestMapping("/sys")
@Tag(name = "登录模块")
public class SysLoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SysConfigService configService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCaptchaCode(), loginBody.getUuid());
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        return R.success(map);
    }


    @Operation(summary = "切换角色")
    @PostMapping("/switch/{roleId}")
    public R<Map<String, Object>> switchRole(@PathVariable("roleId") Long roleId, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(request.getUserPrincipal().getName());
        if (user == null) {
            throw new UserNotExistsException();
        }
        if (Objects.isNull(roleId) || roleId == null) {
            throw new RuntimeException("角色ID不存在!");
        }
        String token = loginService.switchRole(roleId, user.getUserId());
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("token", token);
        return R.success(map);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody RegisterBody registerBody) {
        if (!("true".equalsIgnoreCase(configService.selectConfigByKey("sys.account.register")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        String message = loginService.register(registerBody);
        return StringUtils.isEmpty(message) ? R.success() : R.fail(message);
    }
}
