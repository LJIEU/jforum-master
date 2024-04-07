package com.liu.system.service;

import com.liu.system.dao.SysUser;

import java.util.List;

/**
 * 用户信息服务层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysUserService {
    /**
     * 查询 用户信息 列表
     *
     * @param sysuser 用户信息
     * @return 返回 分页结果
     */
    List<SysUser> selectSysUserList(SysUser sysuser);


    /**
     * 获取 用户信息 详细信息
     *
     * @param userId 用户ID
     * @return 返回用户信息信息
     */
    SysUser selectSysUserByUserId(Long userId);

    /**
     * 新增 用户信息
     *
     * @param sysuser 用户信息
     * @return 添加情况
     */
    int insert(SysUser sysuser);

    /**
     * 修改 用户信息
     *
     * @param sysuser 用户信息
     * @return 修改情况
     */
    int update(SysUser sysuser);

    /**
     * 删除 用户信息
     *
     * @param userIds 用户ID 列表
     * @return 删除情况
     */
    int delete(Long[] userIds);

    /**
     * 更新用户 IP信息
     */
    void updateUserProfile(SysUser sysUser);

    /**
     * 检查用户是否存在
     *
     * @param sysUser 用户信息
     */
    boolean checkUserNameUnique(SysUser sysUser);

    /**
     * 注册用户
     * @param sysUser 用户信息
     * @return 返回是否注册成功
     */
    boolean registerUser(SysUser sysUser);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     */
    SysUser getItemByUserName(String username);
}
