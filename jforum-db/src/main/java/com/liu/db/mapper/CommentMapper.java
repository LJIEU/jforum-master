package com.liu.db.mapper;

import com.liu.db.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论接口层 comment
 *
 * @author JIE
 * @since 2024-05-11
 */
@Mapper
public interface CommentMapper {


    /**
     * 查询 评论 列表
     *
     * @param comment 可以根据字段查询
     * @return 返回 列表集合
     */
    List<Comment> selectCommentList(Comment comment);

    /**
     * 获取 评论 详细信息
     *
     * @param commentId 评论ID
     * @return 返回评论信息
     */
    Comment selectCommentByCommentId(Long commentId);

    /**
     * 新增 评论
     *
     * @param comment 评论
     * @return 添加情况
     */
    int insert(Comment comment);

    /**
     * 修改 评论
     *
     * @param comment 评论
     * @return 修改情况
     */
    int update(Comment comment);

    /**
     * 批量删除 评论
     *
     * @param commentIds 评论ID列表
     * @return 删除情况
     */
    int deleteById(@Param("commentIds") Long[] commentIds);
}
