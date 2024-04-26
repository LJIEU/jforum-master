package com.liu.db.mapper;

import com.liu.db.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子接口层 post
 *
 * @author JIE
 * @since 2024-04-23
 */
@Mapper
public interface PostMapper {


    /**
     * 查询 帖子 列表
     *
     * @param post 可以根据字段查询
     * @return 返回 列表集合
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
     * 批量删除 帖子
     *
     * @param postIds 帖子ID列表
     * @return 删除情况
     */
    int deleteById(@Param("postIds") String[] postIds);
}
