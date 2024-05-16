package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 屏蔽记录对象 sensitive_record
 *
 * @author JIE
 * @since 2024-05-16
 */
@Schema(name = "屏蔽记录--实体类")
public class SensitiveRecord extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @Schema(description = "")
    @ExcelProperty(value = "")
    private Long id;
    /**
     * 被执行过滤的用户ID
     */
    @Schema(description = "被执行过滤的用户ID")
    @ExcelProperty(value = "被执行过滤的用户ID")
    private Long userId;
    /**
     * 被执行过滤的内容ID
     */
    @Schema(description = "被执行过滤的内容ID")
    @ExcelProperty(value = "被执行过滤的内容ID")
    private String contentId;
    /**
     * 类型: 0标题  1:帖子内容  2:评论
     */
    @Schema(description = "类型: 0标题  1:帖子内容  2:评论")
    @ExcelProperty(value = "类型: 0标题  1:帖子内容  2:评论")
    private Integer type;
    /**
     * 被过滤内容
     */
    @Schema(description = "被过滤内容")
    @ExcelProperty(value = "被过滤内容")
    private String filteredContent;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }


    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }


    public void setFilteredContent(String filteredContent) {
        this.filteredContent = filteredContent;
    }

    public String getFilteredContent() {
        return filteredContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("contentId", getContentId())
                .append("type", getType())
                .append("filteredContent", getFilteredContent())
                .append("createTime", getCreateTime())
                .toString();
    }
}