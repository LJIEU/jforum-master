package com.liu.app.controller;

import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import com.liu.db.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 17:14
 */
@Tag(name = "用户模块")
@RequestMapping("/app/user/")
@RestController
public class UserController {
    @Autowired
    private SysUserService userService;

    @Operation(summary = "用户信息")
    @GetMapping("userInfo/{username}")
    public R<UserVo> userInfo(@PathVariable("username") String username) {
        SysUser user = userService.getItemByUserName(username);
        return R.success(sysToUserVo(user));
    }

    private UserVo sysToUserVo(SysUser sysUser) {
        UserVo userVo = new UserVo();
        userVo.setId(sysUser.getUserId());
        userVo.setDeptId(sysUser.getDeptId());
        userVo.setUsername(sysUser.getUserName());
        userVo.setNickname(sysUser.getNickName());
        userVo.setMobile(sysUser.getPhone());
        userVo.setGender(Long.valueOf(sysUser.getSex()));
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setEmail(sysUser.getEmail());
        userVo.setStatus(Objects.equals(sysUser.getStatus(), "0") ? 0 : 1);
        userVo.setCreateTime(sysUser.getCreateTime());
        userVo.setRoleIds(null);
        return userVo;
    }

}
