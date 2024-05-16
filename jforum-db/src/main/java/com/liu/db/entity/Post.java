package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 帖子对象 post
 *
 * @author JIE
 * @since 2024-04-23
 */
@Schema(name = "帖子--实体类")
public class Post extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @ExcelProperty(value = "帖子ID")
    private String postId;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;
    /**
     * 标题
     */
    @Schema(description = "标题")
    @ExcelProperty(value = "标题")
    private String title;
    /**
     * 类型【0:普通  1:置顶】
     */
    @Schema(description = "类型【0:普通  1:置顶】")
    @ExcelProperty(value = "类型【0:普通  1:置顶】")
    private Integer type;
    /**
     * 浏览数
     */
    @Schema(description = "浏览数")
    @ExcelProperty(value = "浏览数")
    private Long views;
    /**
     * 发帖归属地
     */
    @Schema(description = "发帖归属地")
    @ExcelProperty(value = "发帖归属地")
    private String ipAddress;
    /**
     * 排序
     */
    @Schema(description = "排序")
    @ExcelProperty(value = "排序")
    private Integer sort;
    /**
     * 状态【1:已发布 2:待审核 0:隐藏】
     */
    @Schema(description = "状态【1:已发布 2:待审核 0:隐藏--默认 3:审核中 5:驳回】")
    @ExcelProperty(value = "状态【1:已发布 2:待审核 0:隐藏--默认 3:审核中 5:驳回】")
    private String state;
    /**
     * 内容
     */
    @Schema(description = "内容")
    @ExcelProperty(value = "内容")
    private String content;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }


    public void setViews(Long views) {
        this.views = views;
    }

    public Long getViews() {
        return views;
    }


    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }


    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("postId", getPostId())
                .append("userId", getUserId())
                .append("title", getTitle())
                .append("type", getType())
                .append("views", getViews())
                .append("ipAddress", getIpAddress())
                .append("sort", getSort())
                .append("state", getState())
                .append("content", getContent())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("isDelete", getIsDelete())
                .toString();
    }
}