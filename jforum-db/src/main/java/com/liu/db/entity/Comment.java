package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 评论对象 comment
 *
 * @author JIE
 * @since 2024-05-11
 */
@Schema(name = "评论--实体类")
public class Comment extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 评论ID
     */
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

    private List<Comment> children;

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }


    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }


    public void setCommentPid(Long commentPid) {
        this.commentPid = commentPid;
    }

    public Long getCommentPid() {
        return commentPid;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("commentId", getCommentId())
                .append("commentPid", getCommentPid())
                .append("userId", getUserId())
                .append("postId", getPostId())
                .append("content", getContent())
                .append("ip", getIp())
                .append("city", getCity())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("children", getChildren())
                .toString();
    }
}