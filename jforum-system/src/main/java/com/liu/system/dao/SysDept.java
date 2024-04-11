package com.liu.system.dao;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 部门对象 sys_dept
 *
 * @author JIE
 * @since 2024-04-11
 */
@Schema(name = "部门--实体类")
public class SysDept extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    @ExcelProperty(value = "部门ID")
    private Long deptId;
    /**
     * 父ID
     */
    @Schema(description = "父ID")
    @ExcelProperty(value = "父ID")
    private Long parentId;
    /**
     * 深度列表如[1,4,5]路径就是 1-》4-》5
     */
    @Schema(description = "深度列表如[1,4,5]路径就是 1-》4-》5")
    @ExcelProperty(value = "深度列表如[1,4,5]路径就是 1-》4-》5")
    private String ancestors;
    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @ExcelProperty(value = "部门名称")
    private String deptName;
    /**
     * 负责人
     */
    @Schema(description = "负责人")
    @ExcelProperty(value = "负责人")
    private String leader;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @ExcelProperty(value = "联系电话")
    private String phone;
    /**
     * 邮件
     */
    @Schema(description = "邮件")
    @ExcelProperty(value = "邮件")
    private String email;
    /**
     * 顺序
     */
    @Schema(description = "顺序")
    @ExcelProperty(value = "顺序")
    private Integer orderNum;
    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态(0正常 1停用)")
    @ExcelProperty(value = "状态(0正常 1停用)")
    private String status;

    private List<SysDept> children;

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }


    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }


    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getAncestors() {
        return ancestors;
    }


    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }


    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("deptName", getDeptName())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("orderNum", getOrderNum())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("isDelete", getIsDelete())
                .append("remark", getRemark())
                .append("children", getChildren())
                .toString();
    }
}