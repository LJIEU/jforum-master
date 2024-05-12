package com.liu.db.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liu.db.vo.api.AuthorInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/11 18:28
 */
public class CommentVo {
    @Schema(description = "评论ID")
    @ExcelProperty(value = "评论ID")
    private Long commentId;
    /**
     * 父评论ID
     */
    @Schema(description = "父评论ID")
    @ExcelProperty(value = "父评论ID")
    private Long commentPid;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;
    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @ExcelProperty(value = "帖子ID")
    private String postId;
    /**
     * 评论信息
     */
    @Schema(description = "评论信息")
    @ExcelProperty(value = "评论信息")
    private String content;
    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    @ExcelProperty(value = "IP地址")
    private String ip;
    /**
     * 城市
     */
    @Schema(description = "城市")
    @ExcelProperty(value = "城市")
    private String city;

    private AuthorInfo authorInfo;


    List<CommentVo> children;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public AuthorInfo getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfo authorInfo) {
        this.authorInfo = authorInfo;
    }

    public List<CommentVo> getChildren() {
        return children;
    }

    public void setChildren(List<CommentVo> children) {
        this.children = children;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentPid() {
        return commentPid;
    }

    public void setCommentPid(Long commentPid) {
        this.commentPid = commentPid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
