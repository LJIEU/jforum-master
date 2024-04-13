package com.liu.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.github.pagehelper.PageInfo;
import com.liu.core.controller.BaseController;
import com.liu.core.converter.TreeConverter;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.TreeUtils;
import com.liu.system.converter.tree.MenuVoConverter;
import com.liu.system.converter.tree.RoutesVoConverter;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.service.SysMenuService;
import com.liu.system.service.SysUserService;
import com.liu.system.service.relation.SysRoleAndMenuService;
import com.liu.system.service.relation.SysUserAndRoleService;
import com.liu.system.vo.MenuVo;
import com.liu.system.vo.RoutesVo;
import com.liu.system.vo.level.MenuLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限控制层 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "权限菜单")
@RestController
@RequestMapping("/sys/menu")
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
     * @param sysmenu   菜单权限对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysmenu", description = "实体参数")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "menu_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysMenu sysmenu) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysMenu> list = sysmenuService.selectSysMenuList(sysmenu);
        // 获取分页数据
        PageInfo<SysMenu> pageInfo = new PageInfo<>(list);
        // 总页数
        int pages = pageInfo.getPages();
        // 总记录数
        long total = pageInfo.getTotal();
        // 当前页面的总记录数
        int size = pageInfo.getSize();
        clearPage();
        Map<String, Object> map = new HashMap<>(3);
        // 整理数据
        map.put("list", list);
        map.put("total", total);
        map.put("pages", pages);
        return R.success(map);
    }

    /**
     * 获取 路由列表
     */
    @GetMapping("/routes")
    @Operation(summary = "获取路由", description = "获取该登录用户的所有路由列表")
    public R<List<RoutesVo>> routes(HttpServletRequest request) {
        // 获取用户信息
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.getCurrentUser(request));
        if (ObjUtil.isEmpty(user)) {
            return R.fail("用户不存在~");
        }
        List<SysRole> roleByUserId = SpringUtils.getBean(SysUserAndRoleService.class).getRoleByUserId(user.getUserId());
        List<RoutesVo> routes = new ArrayList<>();
        SysRoleAndMenuService roleAndMenuService = SpringUtils.getBean(SysRoleAndMenuService.class);
        for (SysRole sysRole : roleByUserId) {
            List<SysMenu> menus = roleAndMenuService.selectMenuListByRoleId(sysRole.getRoleId()).stream()
                    .filter(v -> "M".equals(v.getMenuType()) || "C".equals(v.getMenuType())).collect(Collectors.toList());
            for (SysMenu menu : menus) {
                RoutesVo routesVo = new RoutesVo();
                routesVo.setId(menu.getMenuId());
                routesVo.setPid(menu.getParentId());
                routesVo.setName(menu.getQuery());
                routesVo.setPath(menu.getPath());
                routesVo.setComponent(menu.getComponent());
                // Meta 的设置
                RoutesVo.Meta meta = new RoutesVo.Meta();
                meta.setTitle(menu.getMenuName());
                meta.setIcon(menu.getIcon());
                meta.setHidden("1".equals(menu.getVisible()));
                meta.setKeepAlive(menu.getIsCache() == 1);
                meta.setBreadcrumb(false);
                // 根据 菜单ID 获取对应的 角色列表
                List<SysRole> sysRoleList = roleAndMenuService.selectRoleByMenuId(menu.getMenuId());
                List<String> roleNameList = sysRoleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
                meta.setRoles(roleNameList);
                routesVo.setMeta(meta);
                routes.add(routesVo);
            }
        }

        // 整理 树型结构
        RoutesVoConverter converter = new RoutesVoConverter();
        List<RoutesVo> tree = TreeUtils.convertTree(routes, converter);
        return R.success(tree);
    }


    /**
     * {
     * value: 1,
     * label: "菜单1",
     * children: [
     * {
     * value: 2,
     * label: "子菜单1-1",
     * },
     * {
     * value: 3,
     * label: "子菜单1-2",
     * },
     * ],
     * }
     */
    @Operation(summary = "获取层级结构")
    @GetMapping("/tree")
    public R<Object> treeLevel() {
        List<SysMenu> menus = sysmenuService.selectSysMenuList(null);
        // 整理 树型结构
//        LevelConverter<SysMenu, MenuLevel> converter = new MenuLevelConverter();
//        LevelUtils<SysMenu, MenuLevel> levelUtils = new LevelUtils<>(menus);
//        List<MenuLevel> menuLevels = levelUtils.buildTree(menus, converter);
//        System.out.println(JSONUtil.toJsonStr(menuLevels));
        // 将其转换为 Level
        List<MenuLevel> levelList = menus.stream().map(v -> menuToLevel(new MenuLevel(), v)).collect(Collectors.toList());

        // UserList ==》 Tree结构
        List<MenuLevel> tree = menus.stream().filter(v -> v.getParentId() == 0L).map(v -> {
            return deep(menus, v, new ArrayList<>(), levelList,
                    menuToLevel(new MenuLevel(), v), new ArrayList<>());
        }).collect(Collectors.toList());

        return R.success(tree);
    }

    private MenuLevel menuToLevel(MenuLevel level, SysMenu menu) {
        level.setValue(menu.getMenuId());
        level.setLabel(menu.getMenuName());
        level.setChildren(new ArrayList<>());
        return level;
    }

    private static MenuLevel deep(List<SysMenu> sysMenuList, SysMenu parent, List<SysMenu> tree,
                                  List<MenuLevel> original, MenuLevel parentLevel, List<MenuLevel> treeLevel) {
        int i = 0;
        for (SysMenu menu : sysMenuList) {
            if (menu != parent && parent.getMenuId().equals(menu.getParentId())) {
                deep(sysMenuList, menu, tree, original, original.get(i), treeLevel);
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(menu);
                if (parentLevel.getChildren() == null) {
                    parentLevel.setChildren(new ArrayList<>());
                }
                parentLevel.getChildren().add(original.get(i));

                tree.add(parent);
                treeLevel.add(parentLevel);
            }
            i++;
        }
        return parentLevel;
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
        ExcelUtil<SysMenu> util = new ExcelUtil<>(SysMenu.class);
        util.exportExcel(response, list, "菜单权限数据", excludeColumnFiledNames);
    }

    @Operation(summary = "获取菜单列表")
    @GetMapping("/menusList")
    public R<Object> menusList(
            @Parameter(name = "status", description = "状态", in = ParameterIn.QUERY)
            @RequestParam("status") String status,
            @Parameter(name = "keywords", description = "关键词[菜单名称]", in = ParameterIn.QUERY)
            @RequestParam("keywords") String keywords) {
        List<SysMenu> sysMenuList = sysmenuService.selectSysMenuListByStatusOrKeywords(status, keywords);
        List<MenuVo> data = sysMenuList.stream().map(this::menuToMenuVo).collect(Collectors.toList());
        TreeConverter<MenuVo> converter = new MenuVoConverter();
        List<MenuVo> treeMenuVo = TreeUtils.convertTree(data, converter);
        // 如果 treeMenuVo 为空 说明没有根菜单  返回原始的数据即可
        if (CollUtil.isNotEmpty(treeMenuVo) || treeMenuVo.size() != 0) {
            return R.success(treeMenuVo);
        }
        return R.success(data);
    }

    /**
     * 获取 菜单权限 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{menuId}")
    public R<MenuVo> getInfo(
            @Parameter(name = "menuId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("menuId") Long menuId) {
        SysMenu sysMenu = sysmenuService.selectSysMenuByMenuId(menuId);
        return R.success(menuToMenuVo(sysMenu));
    }


    /**
     * 新增 菜单权限
     */
    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody MenuVo menuVo, HttpServletRequest request) {
        SysMenu sysMenu = menuVoToMenu(menuVo);
        sysMenu.setCreateBy(SecurityUtils.getCurrentUser(request));
        return R.success(sysmenuService.insert(sysMenu));
    }


    /**
     * 修改 菜单权限
     */
    @Operation(summary = "修改菜单")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody MenuVo menuVo, HttpServletRequest request) {
        SysMenu sysMenu = menuVoToMenu(menuVo);
        sysMenu.setUpdateBy(SecurityUtils.getCurrentUser(request));
        return R.success(sysmenuService.update(sysMenu));
    }

    /**
     * 隐藏菜单
     */
    @Parameters({
            @Parameter(name = "menuId", description = "菜单ID", in = ParameterIn.PATH),
            @Parameter(name = "visible", description = "显示状态(0:显示,1:隐藏)", in = ParameterIn.QUERY)
    })
    @Operation(summary = "修改菜单显示状态")
    @PutMapping("/update/{menuId}")
    public R<Integer> update(
            @PathVariable("menuId") Long menuId,
            @RequestParam("visible") String visible,
            HttpServletRequest request) {
        SysMenu sysMenu = sysmenuService.selectSysMenuByMenuId(menuId);
        if (sysMenu == null) {
            return R.fail("菜单不存在!");
        }
        sysMenu.setVisible(visible);
        sysMenu.setUpdateBy(SecurityUtils.getCurrentUser(request));
        return R.success(sysmenuService.update(sysMenu));
    }


    /**
     * 删除 菜单权限
     * /delete/1,2,3
     */
    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{menuIds}")
    public R<Integer> delete(@PathVariable("menuIds") Long[] menuIds) {
        return R.success(sysmenuService.delete(menuIds));
    }


    private SysMenu menuVoToMenu(MenuVo menuVo) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setChildren(null);
        sysMenu.setMenuId(menuVo.getId());
        sysMenu.setMenuName(menuVo.getName());
        sysMenu.setParentId(menuVo.getParentId());
        sysMenu.setOrderNum(menuVo.getSort());
        sysMenu.setPath(menuVo.getPath());
        sysMenu.setComponent(menuVo.getComponent());
        sysMenu.setQuery(menuVo.getRouting());
        sysMenu.setIsCache(menuVo.getKeepAlive() == 1 ? 1 : 0);
        sysMenu.setMenuType(menuVo.getType());
        sysMenu.setVisible(menuVo.getVisible() == 0 ? "0" : "1");
        sysMenu.setStatus(menuVo.getStatus());
        sysMenu.setPerms(menuVo.getPerm());
        sysMenu.setIcon(menuVo.getIcon());
        sysMenu.setRemark(menuVo.getRemark());
        return sysMenu;
    }


    private MenuVo menuToMenuVo(SysMenu menu) {
        MenuVo menuVo = new MenuVo();
        menuVo.setId(menu.getMenuId());
        menuVo.setParentId(menu.getParentId());
        menuVo.setName(menu.getMenuName());
        menuVo.setType(menu.getMenuType());
        menuVo.setPath(menu.getPath());
        menuVo.setComponent(menu.getComponent());
        menuVo.setPerm(menu.getPerms());
        menuVo.setVisible("0".equals(menu.getVisible()) ? 0 : 1);
        menuVo.setSort(menu.getOrderNum());
        menuVo.setIcon(menu.getIcon());
        menuVo.setKeepAlive(menu.getIsCache());
        menuVo.setAlwaysShow(1);
        menuVo.setStatus(menu.getStatus());
        menuVo.setRemark(menu.getRemark());
        menuVo.setRouting(menu.getQuery());
        return menuVo;
    }

}
