package com.liu.db.service;


import com.liu.db.entity.Post;

import java.util.List;

/**
 * 帖子服务层 post
 *
 * @author JIE
 * @since 2024-04-23
 */
public interface PostService {
    /**
     * 查询 帖子 列表
     *
     * @param post 帖子
     * @return 返回 分页结果
     */
    List<Post> selectPostList(Post post);


    /**
     * 获取 帖子 详细信息
     *
     * @param postId 帖子ID
     * @return 返回帖子信息
     */
    Post selectPostByPostId(String postId);

    /**
     * 新增 帖子
     *
     * @param post 帖子
     * @return 添加情况
     */
    int insert(Post post);

    /**
     * 修改 帖子
     *
     * @param post 帖子
     * @return 修改情况
     */
    int update(Post post);

    /**
     * 删除 帖子
     *
     * @param postIds 帖子ID 列表
     * @return 删除情况
     */
    int delete(String[] postIds);
}
