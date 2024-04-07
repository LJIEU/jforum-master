package com.liu.system.dao;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 角色信息对象 sys_role
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "角色信息--实体类")
public class SysRole extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @ExcelProperty(value = "角色ID")
    private Long roleId;
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    @ExcelProperty(value = "角色名称")
    private String roleName;
    /**
     * 角色权限字符串
     */
    @Schema(description = "角色权限字符串")
    @ExcelProperty(value = "角色权限字符串")
    private String roleKey;
    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    @ExcelProperty(value = "显示顺序")
    private Integer roleSort;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    @ExcelProperty(value = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private String dataScope;
    /**
     * 菜单树选择项是否关联显示
     */
    @Schema(description = "菜单树选择项是否关联显示")
    @ExcelProperty(value = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;
    /**
     * 部门树选择项是否关联显示
     */
    @Schema(description = "部门树选择项是否关联显示")
    @ExcelProperty(value = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;
    /**
     * 角色状态（0正常 1停用）
     */
    @Schema(description = "角色状态（0正常 1停用）")
    @ExcelProperty(value = "角色状态（0正常 1停用）")
    private String status;


    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }


    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleKey() {
        return roleKey;
    }


    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public Integer getRoleSort() {
        return roleSort;
    }


    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getDataScope() {
        return dataScope;
    }


    public void setMenuCheckStrictly(Integer menuCheckStrictly) {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public Integer getMenuCheckStrictly() {
        return menuCheckStrictly;
    }


    public void setDeptCheckStrictly(Integer deptCheckStrictly) {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public Integer getDeptCheckStrictly() {
        return deptCheckStrictly;
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
                .append("roleId", getRoleId())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("roleSort", getRoleSort())
                .append("dataScope", getDataScope())
                .append("menuCheckStrictly", getMenuCheckStrictly())
                .append("deptCheckStrictly", getDeptCheckStrictly())
                .append("status", getStatus())
                .append("isDelete", getIsDelete())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}