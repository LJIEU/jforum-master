package com.liu.system.mapper.relation;

import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 13:31
 */
@Mapper
public interface SysRoleAndMenuMapper {
    /**
     * 根据 角色ID 获取对应的 菜单信息
     *
     * @param roleId 角色ID
     * @return 菜单信息集合
     */
    List<SysMenu> selectMenuListByRoleId(Long roleId);


    /**
     * 根据 菜单ID 获取对应的 角色信息
     *
     * @param menuId 菜单ID
     * @return 角色信息集合
     */
    List<SysRole> selectRoleByMenuId(Long menuId);

    /**
     * 添加数据
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    void insert(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    /**
     * 更新数据
     *
     * @param roleId    角色ID
     * @param oldMenuId 原菜单ID
     * @param newMenuId 新菜单ID
     */
    void update(@Param("roleId") Long roleId,
                @Param("oldMenuId") Long oldMenuId,
                @Param("newMenuId") Long newMenuId);

    /**
     * 删除数据
     *
     * @param roleId    角色ID
     * @param oldMenuId 原菜单ID
     */
    void delete(@Param("roleId") Long roleId,
                @Param("oldMenuId") Long oldMenuId);
}
