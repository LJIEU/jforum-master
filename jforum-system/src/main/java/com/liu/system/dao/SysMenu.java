package com.liu.system.dao;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 菜单权限对象 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "菜单权限--实体类")
public class SysMenu extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    @ExcelProperty(value = "菜单ID")
    private Long menuId;
    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    @ExcelProperty(value = "菜单名称")
    private String menuName;
    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    @ExcelProperty(value = "父菜单ID")
    private Long parentId;
    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;
    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    @ExcelProperty(value = "路由地址")
    private String path;
    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    @ExcelProperty(value = "组件路径")
    private String component;
    /**
     * 路由参数
     */
    @Schema(description = "路由参数")
    @ExcelProperty(value = "路由参数")
    private String query;
    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @Schema(description = "是否缓存（0缓存 1不缓存）")
    @ExcelProperty(value = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;
    /**
     * 菜单类型（M目录 C菜单 F按钮 E外链）
     */
    @Schema(description = "菜单类型（M目录 C菜单 F按钮 E外链）")
    @ExcelProperty(value = "菜单类型（M目录 C菜单 F按钮 E外链）")
    private String menuType;
    /**
     * 菜单状态（0显示 1隐藏）
     */
    @Schema(description = "菜单状态（0显示 1隐藏）")
    @ExcelProperty(value = "菜单状态（0显示 1隐藏）")
    private String visible;
    /**
     * 菜单状态（0正常 1停用）
     */
    @Schema(description = "菜单状态（0正常 1停用）")
    @ExcelProperty(value = "菜单状态（0正常 1停用）")
    private String status;
    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    @ExcelProperty(value = "权限标识")
    private String perms;
    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    @ExcelProperty(value = "菜单图标")
    private String icon;

    private List<SysMenu> children;

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }


    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }


    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }


    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }


    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setIsCache(Integer isCache) {
        this.isCache = isCache;
    }

    public Integer getIsCache() {
        return isCache;
    }


    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuType() {
        return menuType;
    }


    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getPerms() {
        return perms;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("menuId" , getMenuId())
                .append("menuName" , getMenuName())
                .append("parentId" , getParentId())
                .append("orderNum" , getOrderNum())
                .append("path" , getPath())
                .append("component" , getComponent())
                .append("query" , getQuery())
                .append("isCache" , getIsCache())
                .append("menuType" , getMenuType())
                .append("visible" , getVisible())
                .append("status" , getStatus())
                .append("perms" , getPerms())
                .append("icon" , getIcon())
                .append("isDelete" , getIsDelete())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateBy" , getUpdateBy())
                .append("updateTime" , getUpdateTime())
                .append("remark" , getRemark())
                .append("children" , getChildren())
                .toString();
    }
}