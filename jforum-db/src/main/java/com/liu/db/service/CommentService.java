package com.liu.db.service;

import com.liu.db.entity.Comment;
import com.liu.db.entity.SysUser;
import com.liu.db.vo.api.CommentParams;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 评论服务层 comment
 *
 * @author JIE
 * @since 2024-05-11
 */
public interface CommentService {
    /**
     * 查询 评论 列表
     *
     * @param comment 评论
     * @return 返回 分页结果
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
     * @param params  评论信息
     * @param user    用户
     * @param request 请求
     * @return 添加情况
     */
    int insert(CommentParams params, SysUser user, HttpServletRequest request);

    /**
     * 修改 评论
     *
     * @param comment 评论
     * @return 修改情况
     */
    int update(Comment comment);

    /**
     * 删除 评论
     *
     * @param commentIds 评论ID 列表
     * @return 删除情况
     */
    int delete(Long[] commentIds);

}
