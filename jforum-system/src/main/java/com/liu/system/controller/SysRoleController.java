package com.liu.system.controller;

import com.github.pagehelper.PageInfo;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.service.SysRoleService;
import com.liu.system.service.relation.SysRoleAndMenuService;
import com.liu.system.vo.RoleVo;
import com.liu.system.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色信息控制层 sys_role
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "角色接口")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysroleService;

    @Autowired
    private SysRoleAndMenuService roleAndMenuService;

    /**
     * 查询 角色信息 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysrole   角色信息对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysrole", description = "实体参数")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "role_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysRole sysrole) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysRole> list = sysroleService.selectSysRoleList(sysrole);
        List<RoleVo> collect = list.stream().map(v -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setId(v.getRoleId());
            roleVo.setName(v.getRoleName());
            roleVo.setCode(v.getRoleKey());
            roleVo.setStatus(Integer.valueOf(v.getStatus()));
            roleVo.setSort(v.getRoleSort());
            roleVo.setCreateTime(v.getCreateTime());
            roleVo.setUpdateTime(v.getUpdateTime());
            roleVo.setRemark(v.getRemark());
            return roleVo;
        }).collect(Collectors.toList());
        // 获取分页数据
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        // 总页数
        int pages = pageInfo.getPages();
        // 总记录数
        long total = pageInfo.getTotal();
        // 当前页面的总记录数
        int size = pageInfo.getSize();
        Map<String, Object> map = new HashMap<>(3);
        // 整理数据
        map.put("list", collect);
        map.put("total", total);
        map.put("pages", pages);
        clearPage();
        return R.success(map);
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
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        util.exportExcel(response, list, "角色信息数据", excludeColumnFiledNames);
    }


    /**
     * 获取 角色信息 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{roleId}")
    public R<RoleVo> getInfo(
            @Parameter(name = "roleId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("roleId") Long roleId) {
        SysRole sysRole = sysroleService.selectSysRoleByRoleId(roleId);
        if (sysRole == null) {
            return R.fail();
        }
        return R.success(sysRoleToRoleVo(sysRole));
    }


    /**
     * 获取 角色菜单ID集合
     */
    @Operation(summary = "根据ID获取菜单ID集合")
    @GetMapping("/{roleId}/menus")
    public R<List<Long>> getMenuId(
            @Parameter(name = "roleId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("roleId") Long roleId) {
        List<Long> menusId = roleAndMenuService.selectMenuListByRoleId(roleId).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        return R.success(menusId);
    }


    /**
     * 获取 角色 下拉列表
     */
    @Operation(summary = "获取 角色 下拉列表")
    @GetMapping("/options")
    public R<List<Level>> options() {
        List<Level> result = sysroleService.selectSysRoleList(null).stream().map(v -> {
            Level level = new Level();
            level.setValue(v.getRoleId());
            level.setLabel(v.getRoleName());
            return level;
        }).collect(Collectors.toList());
        return R.success(result);
    }


    /**
     * 修改 角色状态
     */
    @Operation(summary = "根据ID修改角色状态")
    @PutMapping("/{roleId}/status")
    public R<SysRole> getInfo(
            @Parameter(name = "roleId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("roleId") Long roleId,
            @Parameter(name = "status", description = "状态", in = ParameterIn.QUERY)
            @RequestParam("status") String status) {
        sysroleService.updateStatus(roleId, status);
        return R.success();
    }

    /**
     * 分配菜单包括按钮权限
     */
    @Operation(summary = "分配菜单")
    @PutMapping("/{roleId}/assignMenus")
//    @Log(describe = "赋权操作", operatorType = OperatorType.MANAGE)
//    @PreAuthorize("@authority.hasPermission('system:role:add,system:menu:add,system:menu:update,system:menu:delete')")
    public R<String> assignMenus(
            @PathVariable("roleId") Long roleId,
            @RequestParam("menusIds") Long[] menusIds, HttpServletRequest request) {
        String username = SecurityUtils.getCurrentUser(request);
        roleAndMenuService.assignMenus(roleId, menusIds, username);
        return R.success();
    }

    /**
     * 新增 角色信息
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<String> add(@Valid @RequestBody RoleVo roleVo) {
        SysRole sysRole = roleVoToSysRole(roleVo);
        sysroleService.insert(sysRole);
        return R.success();
    }


    /**
     * 修改 角色信息
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<String> update(@Valid @RequestBody RoleVo roleVo) {
        SysRole sysRole = roleVoToSysRole(roleVo);
        sysroleService.update(sysRole);
        return R.success();
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

    /**
     * 转换数据
     */
    private SysRole roleVoToSysRole(@Valid RoleVo roleVo) {
        SysRole sysRole = new SysRole();
        if (roleVo.getId() != null) {
            sysRole.setRoleId(roleVo.getId());
        }
        sysRole.setRoleName(roleVo.getName());
        sysRole.setRoleKey(roleVo.getCode());
        sysRole.setRoleSort(roleVo.getSort());
        sysRole.setStatus(roleVo.getStatus() == 0 ? "0" : "1");
        sysRole.setRoleSort(roleVo.getDataScope());
        sysRole.setRemark(roleVo.getRemark());

        return sysRole;
    }

    /**
     * 转换数据
     */
    private RoleVo sysRoleToRoleVo(SysRole role) {
        RoleVo roleVo = new RoleVo();
        roleVo.setId(role.getRoleId());
        roleVo.setName(role.getRoleName());
        roleVo.setCode(role.getRoleKey());
        roleVo.setSort(role.getRoleSort());
        roleVo.setStatus(Integer.valueOf(role.getStatus()));
        roleVo.setCreateTime(role.getCreateTime());
        roleVo.setUpdateTime(role.getUpdateTime());
        roleVo.setRemark(role.getRemark());
        roleVo.setDataScope(Integer.valueOf(role.getDataScope()));
        return roleVo;
    }

}
