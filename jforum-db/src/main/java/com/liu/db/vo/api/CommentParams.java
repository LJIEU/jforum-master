package com.liu.db.vo.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/11 19:25
 */
public class CommentParams {
    @Schema(description = "回复目标[默认0 顶级评论]")
    private Long pid;
    @NotBlank(message = "帖子ID不允许为空")
    @Schema(description = "帖子ID")
    private String postId;
    @NotBlank(message = "内容不允许为空")
    @Schema(description = "评论内容")
    private String content;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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
}
