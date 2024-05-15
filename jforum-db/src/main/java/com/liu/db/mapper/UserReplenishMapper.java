package com.liu.db.mapper;

import com.liu.db.entity.UserReplenish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户补充信息接口层 user_replenish
 *
 * @author JIE
 * @since 2024-05-15
 */
@Mapper
public interface UserReplenishMapper {


    /**
     * 查询 用户补充信息 列表
     *
     * @param userreplenish 可以根据字段查询
     * @return 返回 列表集合
     */
    List<UserReplenish> selectUserReplenishList(UserReplenish userreplenish);

    /**
     * 获取 用户补充信息 详细信息
     *
     * @param userId 用户补充信息ID
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
     * 批量删除 用户补充信息
     *
     * @param userIds 用户ID列表
     * @return 删除情况
     */
    int deleteById(@Param("userIds") Long[] userIds);
}
