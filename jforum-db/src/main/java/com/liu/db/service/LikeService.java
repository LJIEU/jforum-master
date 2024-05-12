package com.liu.db.service;


import com.liu.db.entity.Like;

import java.util.List;

/**
 * 点赞服务层 like
 *
 * @author JIE
 * @since 2024-05-11
 */
public interface LikeService {
    /**
     * 查询 点赞 列表
     *
     * @param like 点赞
     * @return 返回 分页结果
     */
    List<Like> selectLikeList(Like like);


    /**
     * 获取 点赞 详细信息
     *
     * @param likeId 点赞ID
     * @return 返回点赞信息
     */
    Like selectLikeByLikeId(Long likeId);

    /**
     * 新增 点赞
     *
     * @param like 点赞
     * @return 添加情况
     */
    int insert(Like like);

    /**
     * 修改 点赞
     *
     * @param like 点赞
     * @return 修改情况
     */
    int update(Like like);

    /**
     * 删除 点赞
     *
     * @param likeIds 点赞ID 列表
     * @return 删除情况
     */
    int delete(Long[] likeIds);
}
