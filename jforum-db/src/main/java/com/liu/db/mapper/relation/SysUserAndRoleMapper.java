package com.liu.db.mapper.relation;

import com.liu.db.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 11:45
 */
@Mapper
public interface SysUserAndRoleMapper {
    /**
     * 根据用户ID 获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getRoleByUserId(Long userId);

    /**
     * 数据更新
     *
     * @param oldRoleId 之前的ID
     * @param newRoleId 新的ID
     * @param userId    用户ID
     */
    void update(@Param("oldRoleId") Long oldRoleId, @Param("newRoleId") Long newRoleId, @Param("userId") Long userId);

    /**
     * 添加数据
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void add(@Param("roleId") Long roleId, @Param("userId") Long userId);

    /**
     * 移除
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void delete(@Param("roleId") Long roleId, @Param("userId") Long userId);
}
