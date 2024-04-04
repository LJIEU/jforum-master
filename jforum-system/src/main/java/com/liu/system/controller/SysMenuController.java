package com.liu.system.controller;

import cn.hutool.core.collection.CollUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.converter.TreeConverter;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.TreeUtils;
import com.liu.system.converter.SysMenuConverter;
import com.liu.system.dao.SysMenu;
import com.liu.system.service.SysMenuService;
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
 * 菜单权限控制层 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "权限菜单")
@RestController
@RequestMapping("/system/sysmenu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysmenuService;

    /**
     * 查询 菜单权限 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysmenu  菜单权限对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
            @Parameter(name = "sysmenu" , description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysMenu>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "menu_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
        SysMenu sysmenu) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysMenu> list = sysmenuService.selectSysMenuList(sysmenu);
        clearPage();
        return R.success(list);
    }

        /**
         * 树型结构 数据
         */
        @Operation(summary = "获取树型结构")
        @GetMapping("/tree")
        public R<List<SysMenu>> list(SysMenu sysmenu) {
            List<SysMenu> list = sysmenuService.selectSysMenuList(sysmenu);
            // 整理成 树型结构
            TreeConverter<SysMenu> converter = new SysMenuConverter();
            List<SysMenu> treeList = TreeUtils.convertTree(list, converter);
            if (CollUtil.isEmpty(treeList)) {
                return R.success(list);
            } else {
                return R.success(treeList);
            }
        }

    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysMenu sysmenu) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysMenu> list = sysmenuService.selectSysMenuList(sysmenu);
        ExcelUtil<SysMenu> util = new ExcelUtil<>(SysMenu. class);
        util.exportExcel(response, list, "菜单权限数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 菜单权限 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{menuId}")
    public R<SysMenu> getInfo(
            @Parameter(name = "menuId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("menuId") Long menuId) {
        return R.success(sysmenuService.selectSysMenuByMenuId(menuId));
    }


    /**
     * 新增 菜单权限
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysMenu sysmenu) {
        return R.success(sysmenuService.insert(sysmenu));
    }


    /**
     * 修改 菜单权限
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysMenu sysmenu) {
        return R.success(sysmenuService.update(sysmenu));
    }


    /**
     * 删除 菜单权限
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{menuIds}")
    public R<Integer> delete(@PathVariable("menuIds") Long[] menuIds) {
        return R.success(sysmenuService.delete(menuIds));
    }


}
