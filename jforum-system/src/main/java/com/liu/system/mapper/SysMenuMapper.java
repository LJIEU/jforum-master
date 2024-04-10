package com.liu.system.mapper;

import com.liu.system.dao.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单权限接口层 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
@Mapper
public interface SysMenuMapper {


    /**
     * 查询 菜单权限 列表
     *
     * @param sysmenu 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysMenu> selectSysMenuList(SysMenu sysmenu);

    /**
     * 获取 菜单权限 详细信息
     *
     * @param menuId 菜单权限ID
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
     * 批量删除 菜单权限
     *
     * @param menuId 菜单ID
     * @return 删除情况
     */
    int deleteById(Long menuId);
}
