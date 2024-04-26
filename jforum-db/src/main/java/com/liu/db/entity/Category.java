package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 帖子分类对象 category
 *
 * @author JIE
 * @since 2024-04-23
 */
@Schema(name = "帖子分类--实体类")
public class Category extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    @ExcelProperty(value = "分类ID")
    private Long categoryId;
    /**
     * 名称
     */
    @Schema(description = "名称")
    @ExcelProperty(value = "名称")
    private String name;
    /**
     * 父ID
     */
    @Schema(description = "父ID")
    @ExcelProperty(value = "父ID")
    private Long pid;
    /**
     * 排序
     */
    @Schema(description = "排序")
    @ExcelProperty(value = "排序")
    private Integer sort;

    private List<Category> children;

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }


    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getPid() {
        return pid;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("categoryId", getCategoryId())
                .append("name", getName())
                .append("pid", getPid())
                .append("sort", getSort())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .append("children", getChildren())
                .toString();
    }
}