package com.liu.db.service;


import com.liu.db.entity.UserReplenish;

import java.util.List;

/**
 * 用户补充信息服务层 user_replenish
 *
 * @author JIE
 * @since 2024-05-15
 */
public interface UserReplenishService {
    /**
     * 查询 用户补充信息 列表
     *
     * @param userreplenish 用户补充信息
     * @return 返回 分页结果
     */
    List<UserReplenish> selectUserReplenishList(UserReplenish userreplenish);


    /**
     * 获取 用户补充信息 详细信息
     *
     * @param userId 用户ID
     * @return 返回用户补充信息信息
     */
    UserReplenish selectUserReplenishByUserId(Long userId);

    /**
     * 新增 用户补充信息
     *
     * @param userreplenish 用户补充信息
     * @return 添加情况
     */
    int insert(UserReplenish userreplenish);

    /**
     * 修改 用户补充信息
     *
     * @param userreplenish 用户补充信息
     * @return 修改情况
     */
    int update(UserReplenish userreplenish);

    /**
     * 删除 用户补充信息
     *
     * @param userIds 用户ID 列表
     * @return 删除情况
     */
    int delete(Long[] userIds);
}
