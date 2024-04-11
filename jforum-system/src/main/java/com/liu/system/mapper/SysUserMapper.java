package com.liu.system.mapper;

import com.liu.system.dao.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户信息接口层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Mapper
public interface SysUserMapper {


    /**
     * 查询 用户信息 列表
     *
     * @param sysuser 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysUser> selectSysUserList(SysUser sysuser);

    /**
     * 获取 用户信息 详细信息
     *
     * @param userId 用户信息ID
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
     * @param userId 用户ID
     * @return 删除情况
     */
    int deleteById(Long userId);

    /**
     * 更新用户 IP信息
     */
    void updateUserProfile(SysUser sysUser);

    /**
     * 检查用户是否存在
     *
     * @param userName 用户信息
     */
    SysUser checkUserNameUnique(String userName);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     */
    SysUser selectSysUserByUserName(String username);
}
