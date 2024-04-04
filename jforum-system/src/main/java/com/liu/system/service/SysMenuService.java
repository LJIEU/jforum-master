package com.liu.system.service;

import com.liu.system.dao.SysMenu;

import java.util.List;

/**
 * 菜单权限服务层 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysMenuService {
    /**
     * 查询 菜单权限 列表
     *
     * @param sysmenu 菜单权限
     * @return 返回 分页结果
     */
    List<SysMenu> selectSysMenuList(SysMenu sysmenu);


    /**
     * 获取 菜单权限 详细信息
     *
     * @param menuId 菜单ID
     * @return 返回菜单权限信息
     */
        SysMenu selectSysMenuByMenuId(Long menuId);

    /**
     * 新增 菜单权限
     *
     * @param sysmenu 菜单权限
     * @return 添加情况
     */
    int insert(SysMenu sysmenu);

    /**
     * 修改 菜单权限
     *
     * @param sysmenu 菜单权限
     * @return 修改情况
     */
    int update(SysMenu sysmenu);

    /**
     * 删除 菜单权限
     *
     * @param menuIds 菜单ID 列表
     * @return 删除情况
     */
    int delete(Long[] menuIds);
}
