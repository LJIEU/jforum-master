package com.liu.security.service;

import com.liu.core.model.BaseUser;

import java.util.Set;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:24
 */
public interface BaseService<T> {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     */
    BaseUser selectSysUserByUserName(String username);

    Set<String> getMenuPermission(BaseUser user);
}
