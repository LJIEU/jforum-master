package com.liu.db.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/24 10:04
 */
public class PostVo {
    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    String id;
    /**
     * 标题
     */
    @Schema(description = "标题")
    String title;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    String username;
    /**
     * 用户名
     */
    @Schema(description = "昵称")
    String nickname;
    /**
     * 内容
     */
    @Schema(description = "内容")
    String content;
    /**
     * 状态 1已发布 2待审核 3隐私
     */
    @Schema(description = "状态 1已发布 2待审核 3隐私")
    Integer state;
    /**
     * 分类
     */
    @Schema(description = "分类")
    String categoryName;
    /**
     * 分类
     */
    @Schema(description = "分类ID")
    Long category;
    /**
     * 归属地
     */
    @Schema(description = "归属地")
    String place;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;
    /**
     * 审核通过时间
     */
    @Schema(description = "审核通过时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date releaseTime;

    /**
     * 类型  0普通 1置顶
     */
    @Schema(description = "类型 0普通 1置顶")
    Integer type;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    Integer likeNum;
    /**
     * 评论数
     */
    @Schema(description = "评论数")
    Integer commentNum;
    /**
     * 综合评分
     */
    @Schema(description = "综合评分")
    Integer overallRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
