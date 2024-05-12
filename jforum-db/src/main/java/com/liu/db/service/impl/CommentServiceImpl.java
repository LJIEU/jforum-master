package com.liu.db.service.impl;

import cn.hutool.core.util.StrUtil;
import com.liu.core.excption.ServiceException;
import com.liu.core.utils.AddressUtils;
import com.liu.core.utils.IpUtils;
import com.liu.db.entity.Comment;
import com.liu.db.entity.Post;
import com.liu.db.entity.SysUser;
import com.liu.db.mapper.CommentMapper;
import com.liu.db.service.CommentService;
import com.liu.db.service.PostService;
import com.liu.db.vo.api.CommentParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论实际业务层 comment
 *
 * @author JIE
 * @since 2024-05-11
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostService postService;

    @Override
    public List<Comment> selectCommentList(Comment comment) {
        return commentMapper.selectCommentList(comment);
    }

    @Override
    public Comment selectCommentByCommentId(Long commentId) {
        return commentMapper.selectCommentByCommentId(commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(CommentParams params, SysUser user, HttpServletRequest request) {
        Comment comment = new Comment();
        if (params.getPid() != null && params.getPid() != 0L) {
            // 去查询该父评论是否存在
            Comment pComment = selectCommentByCommentId(params.getPid());
            if (pComment == null) {
                throw new ServiceException("评论异常");
            }
        }
        // 去判断帖子是否存在
        if (params.getPostId() != null && StrUtil.isNotEmpty(params.getPostId())) {
            Post post = postService.selectPostByPostId(params.getPostId());
            if (post == null) {
                throw new ServiceException("帖子不存在!");
            }
        }
        // 不允许空评论
        if (params.getContent() == null || StrUtil.isEmpty(params.getContent())) {
            throw new ServiceException("空评论");
        }
        // 用户信息
        comment.setUserId(user.getUserId());
        // 评论信息
        comment.setCommentPid(params.getPid());
        comment.setPostId(params.getPostId());
        // TODO 2024/5/11/19:47 后期需要对评论 进行屏蔽词过滤
        comment.setContent(params.getContent());
        // 获取IP
        comment.setIp(IpUtils.getIpAddress(request));
        comment.setCity(AddressUtils.getRealAddressByIp(comment.getIp()));

        return commentMapper.insert(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Comment comment) {
        return commentMapper.update(comment);
    }

    @Override
    public int delete(Long[] commentIds) {
        return commentMapper.deleteById(commentIds);
    }
}
