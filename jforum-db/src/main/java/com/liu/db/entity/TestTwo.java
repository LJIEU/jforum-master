package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 测试对象 test_two
 *
 * @author JIE
 * @since 2024-04-02
 */
@Schema(name = "测试--实体类")
public class TestTwo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @Schema(description = "ID")
    @ExcelProperty(value = "ID")
    private Long twoId;
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @ExcelProperty(value = "姓名")
    private String twoName;
    /**
     * 父ID
     */
    @Schema(description = "父ID")
    @ExcelProperty(value = "父ID")
    private Long pid;


    private List<TestTwo> children;

    public List<TestTwo> getChildren() {
        return children;
    }

    public void setChildren(List<TestTwo> children) {
        this.children = children;
    }

    public void setTwoId(Long twoId) {
        this.twoId = twoId;
    }

    public Long getTwoId() {
        return twoId;
    }


    public void setTwoName(String twoName) {
        this.twoName = twoName;
    }

    public String getTwoName() {
        return twoName;
    }


    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("twoId" , getTwoId())
                .append("twoName" , getTwoName())
                .append("pid" , getPid())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateBy" , getUpdateBy())
                .append("updateTime" , getUpdateTime())
                .append("isDelete" , getIsDelete())
                .toString();
    }
}