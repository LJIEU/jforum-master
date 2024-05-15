package com.liu.app.controller;

import cn.hutool.core.util.StrUtil;
import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.result.R;
import com.liu.core.utils.SecurityUtils;
import com.liu.db.entity.SysUser;
import com.liu.db.entity.UserReplenish;
import com.liu.db.service.SysUserService;
import com.liu.db.service.UserReplenishService;
import com.liu.db.vo.UserInfoVo;
import com.liu.db.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserReplenishService replenishService;

    @Operation(summary = "用户信息")
    @GetMapping("userInfo/{username}")
    public R<UserVo> userInfo(@PathVariable("username") String username) {
        SysUser user = userService.getItemByUserName(username);
        UserReplenish userReplenish = replenishService.selectUserReplenishByUserId(user.getUserId());
        return R.success(sysToUserVo(user, userReplenish));
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("/updateUser")
    public R<String> updateUser(@RequestBody UserInfoVo userInfoVo, HttpServletRequest request) {
        SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new UserNotExistsException();
        }
        // 用户名是不能更改的 只能更改 昵称
        if (StrUtil.isNotEmpty(userInfoVo.getNickname())) {
            user.setNickName(userInfoVo.getNickname());
        }
        // 头像
        if (StrUtil.isNotEmpty(userInfoVo.getAvatarUrl())) {
            user.setAvatar(userInfoVo.getAvatarUrl());
        }
        // 性别
        if (StrUtil.isNotEmpty(userInfoVo.getSex())) {
            user.setSex(userInfoVo.getSex());
        }

        /*设置 扩充信息 背景 个性签名 地址 行业*/
        UserReplenish replenish = replenishService.selectUserReplenishByUserId(user.getUserId());
        if (replenish == null) {
            replenish = new UserReplenish();
            replenish.setUserId(user.getUserId());
            replenish.setUserName(user.getUserName());
        }
        // 背景
        if (StrUtil.isNotEmpty(userInfoVo.getBackgroundUrl())) {
            replenish.setBackgroundUrl(userInfoVo.getBackgroundUrl());
        }
        // 个性签名
        if (StrUtil.isNotEmpty(userInfoVo.getSignature())) {
            replenish.setSignature(userInfoVo.getSignature());
        }
        // 地址
        // TODO 2024/5/15/17:55 后期可能会添加减操作
        if (StrUtil.isNotEmpty(userInfoVo.getAddress())) {
            // 整理地址
            String address = userInfoVo.getAddress().replace("|", "");
            String str = replenish.getAddressArr();
            String[] addressArr = str.split("\\|");
            boolean flag = false;
            for (String v : addressArr) {
                if (v.equals(address)) {
                    flag = true;
                    break;
                }
            }
            // 不存在才添加地址
            if (!flag) {
                replenish.setAddressArr(replenish.getAddressArr() + "|" + address);
            }
        }
        // 行业
        if (StrUtil.isNotEmpty(userInfoVo.getIndustry())) {
            replenish.setIndustryValue(userInfoVo.getIndustry());
        }

        // 插入
        userService.insert(user);
        replenishService.insert(replenish);
        return R.success();
    }


    private UserVo sysToUserVo(SysUser sysUser, UserReplenish replenish) {
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
        // 扩展
        userVo.setAddressArr(StrUtil.isNotEmpty(replenish.getAddressArr()) ? replenish.getAddressArr().split("\\|") : null);
        userVo.setBackgroundUrl(replenish.getBackgroundUrl());
        userVo.setIndustryValue(replenish.getIndustryValue());
        userVo.setSignature(replenish.getSignature());
        return userVo;
    }

}
