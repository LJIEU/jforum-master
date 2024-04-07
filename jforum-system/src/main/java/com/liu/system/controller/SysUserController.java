package com.liu.system.controller;


import cn.hutool.core.util.ObjUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.service.SysUserService;
import com.liu.system.service.relation.SysUserAndRoleService;
import com.liu.system.vo.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户信息控制层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/system/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysuserService;

    @Autowired
    private SysUserAndRoleService sysUserAndRoleService;

    /**
     * 查询 用户信息 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysuser   用户信息对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysuser", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysUser>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "user_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysUser sysuser) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysUser> list = sysuserService.selectSysUserList(sysuser);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysUser sysuser) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysUser> list = sysuserService.selectSysUserList(sysuser);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.exportExcel(response, list, "用户信息数据", excludeColumnFiledNames);
    }


    /**
     * 获取 用户信息 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{userId}")
    public R<SysUser> getInfo(
            @Parameter(name = "userId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("userId") Long userId) {
        return R.success(sysuserService.selectSysUserByUserId(userId));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    public R<UserInfo> getUserInfo(Principal principal) {
        String username = principal.getName();
        SysUser sysUser = sysuserService.getItemByUserName(username);
        if (ObjUtil.isEmpty(sysUser)) {
            return R.fail("用户不存在");
        }
        // 包装数据
        UserInfo userInfo = new UserInfo().setUserId(sysUser.getUserId())
                .setUsername(sysUser.getUserName())
                .setNickname(sysUser.getNickName())
                .setDeptId(sysUser.getDeptId())
                .setAvatar(sysUser.getAvatar());
        // 角色
        List<SysRole> sysRoleList = sysUserAndRoleService.getRoleByUserId(sysUser.getUserId());
        userInfo.setRoleInfo(sysRoleList);
        return R.success(userInfo);
    }


    /**
     * 新增 用户信息
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysUser sysuser) {
        return R.success(sysuserService.insert(sysuser));
    }


    /**
     * 修改 用户信息
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysUser sysuser) {
        return R.success(sysuserService.update(sysuser));
    }


    /**
     * 删除 用户信息
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{userIds}")
    public R<Integer> delete(@PathVariable("userIds") Long[] userIds) {
        return R.success(sysuserService.delete(userIds));
    }


}
