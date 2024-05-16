package com.liu.db.service;


import com.liu.db.entity.Post;
import com.liu.db.vo.api.PostParam;
import jakarta.servlet.http.HttpServletRequest;

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

    /**
     * 创建帖子
     *
     * @param param   帖子信息
     * @param request 请求
     * @param userId  用户ID
     */
    void createPost(PostParam param, HttpServletRequest request, Long userId);

    /**
     * 浏览记录数+1
     * @param post 帖子
     */
    void updateViews(Post post);

    /**
     * 待处理的帖子列表 [只获取20条]
     * @return 返回待审核的帖子列表 并且是根据时间升序的[优先处理老的]
     * @param state 状态
     */
    List<Post> pendingPostList(String state);

    /**
     * 修改帖子
     * @param param 参数
     * @param request 请求
     * @param userId 用户ID
     */
    void update(PostParam param, HttpServletRequest request, Long userId);
}
