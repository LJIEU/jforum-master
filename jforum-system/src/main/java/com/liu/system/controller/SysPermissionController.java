package com.liu.system.controller;

import cn.hutool.core.util.ObjUtil;
import com.liu.core.result.R;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.service.SysUserService;
import com.liu.system.service.relation.SysRoleAndMenuService;
import com.liu.system.service.relation.SysUserAndRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 权限模块
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 13:24
 */
@Tag(name = "权限管理")
@RequestMapping("/sys/permission")
@RestController
public class SysPermissionController {

    @Autowired
    private SysRoleAndMenuService roleAndMenuService;

    @Operation(summary = "获取当前登录用户的权限标识")
    @GetMapping("/getUserPermission")
    public R<List<String>> getUserPermission(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        SysUserService userService = SpringUtils.getBean(SysUserService.class);
        SysUser user = userService.getItemByUserName(username);
        if (ObjUtil.isEmpty(user)) {
            return R.fail("用户不存在");
        }
        SysUserAndRoleService userAndRoleService = SpringUtils.getBean(SysUserAndRoleService.class);
        List<SysRole> roleByUserId = userAndRoleService.getRoleByUserId(user.getUserId());
        // 角色对应权限菜单
        List<String> permissionList = new ArrayList<>();
        roleByUserId.forEach(v -> {
            List<SysMenu> menuList = roleAndMenuService.selectMenuListByRoleId(v.getRoleId());
            menuList.forEach(m -> {
                permissionList.add(m.getPerms());
            });
        });
        return R.success(permissionList);
    }
}
