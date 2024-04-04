package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.system.dao.SysRole;
import com.liu.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色信息控制层 sys_role
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "角色")
@RestController
@RequestMapping("/system/sysrole")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysroleService;

    /**
     * 查询 角色信息 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysrole  角色信息对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
            @Parameter(name = "sysrole" , description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysRole>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "role_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
        SysRole sysrole) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysRole> list = sysroleService.selectSysRoleList(sysrole);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysRole sysrole) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysRole> list = sysroleService.selectSysRoleList(sysrole);
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole. class);
        util.exportExcel(response, list, "角色信息数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 角色信息 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{roleId}")
    public R<SysRole> getInfo(
            @Parameter(name = "roleId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("roleId") Long roleId) {
        return R.success(sysroleService.selectSysRoleByRoleId(roleId));
    }


    /**
     * 新增 角色信息
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysRole sysrole) {
        return R.success(sysroleService.insert(sysrole));
    }


    /**
     * 修改 角色信息
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysRole sysrole) {
        return R.success(sysroleService.update(sysrole));
    }


    /**
     * 删除 角色信息
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{roleIds}")
    public R<Integer> delete(@PathVariable("roleIds") Long[] roleIds) {
        return R.success(sysroleService.delete(roleIds));
    }


}
