package com.liu.system.service;

import com.liu.system.dao.SysRole;

import java.util.List;

/**
 * 角色信息服务层 sys_role
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysRoleService {
    /**
     * 查询 角色信息 列表
     *
     * @param sysrole 角色信息
     * @return 返回 分页结果
     */
    List<SysRole> selectSysRoleList(SysRole sysrole);


    /**
     * 获取 角色信息 详细信息
     *
     * @param roleId 角色ID
     * @return 返回角色信息信息
     */
    SysRole selectSysRoleByRoleId(Long roleId);

    /**
     * 新增 角色信息
     *
     * @param sysrole 角色信息
     * @return 添加情况
     */
    int insert(SysRole sysrole);

    /**
     * 修改 角色信息
     *
     * @param sysrole 角色信息
     * @return 修改情况
     */
    int update(SysRole sysrole);

    /**
     * 删除 角色信息
     *
     * @param roleIds 角色ID 列表
     * @return 删除情况
     */
    int delete(Long[] roleIds);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 角色状态
     */
    void updateStatus(Long roleId, String status);

    /**
     * 根据 角色名称 获取 角色ID
     *
     * @param roleName 角色名称
     * @return 返回 ID
     */
    Long getItem(String roleName);
}
