package com.liu.system.mapper.relation;

import com.liu.system.dao.SysRole;
import org.apache.ibatis.annotations.Mapper;

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
    void update(Long oldRoleId, Long newRoleId, Long userId);

    /**
     * 添加数据
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void add(Long roleId, Long userId);

    /**
     * 移除
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    void delete(Long roleId, Long userId);
}
