package com.liu.db.vo;

import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 22:14
 */
public class RoutesVo {
    private Long id;
    private Long pid;
    private String name;
    private String path;
    private String component;
    private Integer orderNum;
    private List<RoutesVo> children;

    private Meta meta;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoutesVo routesVo)) {
            return false;
        }
        return Objects.equals(getId(), routesVo.getId()) && Objects.equals(getPid(), routesVo.getPid()) && Objects.equals(getName(), routesVo.getName()) && Objects.equals(getPath(), routesVo.getPath()) && Objects.equals(getComponent(), routesVo.getComponent()) && Objects.equals(getOrderNum(), routesVo.getOrderNum()) && Objects.equals(getChildren(), routesVo.getChildren()) && Objects.equals(getMeta(), routesVo.getMeta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPid(), getName(), getPath(), getComponent(), getOrderNum(), getChildren(), getMeta());
    }

    /*    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RoutesVo other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                label107:
                {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label107;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label107;
                    }

                    return false;
                }

                Object this$pid = this.getPid();
                Object other$pid = other.getPid();
                if (this$pid == null) {
                    if (other$pid != null) {
                        return false;
                    }
                } else if (!this$pid.equals(other$pid)) {
                    return false;
                }

                Object this$orderNum = this.getOrderNum();
                Object other$orderNum = other.getOrderNum();
                if (this$orderNum == null) {
                    if (other$orderNum != null) {
                        return false;
                    }
                } else if (!this$orderNum.equals(other$orderNum)) {
                    return false;
                }

                label86:
                {
                    Object this$name = this.getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label86;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label86;
                    }

                    return false;
                }

                label79:
                {
                    Object this$path = this.getPath();
                    Object other$path = other.getPath();
                    if (this$path == null) {
                        if (other$path == null) {
                            break label79;
                        }
                    } else if (this$path.equals(other$path)) {
                        break label79;
                    }

                    return false;
                }

                label72:
                {
                    Object this$component = this.getComponent();
                    Object other$component = other.getComponent();
                    if (this$component == null) {
                        if (other$component == null) {
                            break label72;
                        }
                    } else if (this$component.equals(other$component)) {
                        break label72;
                    }

                    return false;
                }

                Object this$children = this.getChildren();
                Object other$children = other.getChildren();
                if (this$children == null) {
                    if (other$children != null) {
                        return false;
                    }
                } else if (!this$children.equals(other$children)) {
                    return false;
                }

                Object this$meta = this.getMeta();
                Object other$meta = other.getMeta();
                if (this$meta == null) {
                    return other$meta == null;
                } else {
                    return this$meta.equals(other$meta);
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoutesVo;
    }


    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $pid = this.getPid();
        result = result * 59 + ($pid == null ? 43 : $pid.hashCode());
        Object $orderNum = this.getOrderNum();
        result = result * 59 + ($orderNum == null ? 43 : $orderNum.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $path = this.getPath();
        result = result * 59 + ($path == null ? 43 : $path.hashCode());
        Object $component = this.getComponent();
        result = result * 59 + ($component == null ? 43 : $component.hashCode());
        Object $children = this.getChildren();
        result = result * 59 + ($children == null ? 43 : $children.hashCode());
        Object $meta = this.getMeta();
        result = result * 59 + ($meta == null ? 43 : $meta.hashCode());
        return result;
    }*/

    public static class Meta {
        /**
         * 菜单名称
         */
        private String title;
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 菜单是否隐藏
         */
        private Boolean hidden;
        /**
         * 是否固定页签
         */
        private Boolean affix;
        /**
         * 是否缓存页面
         */
        private Boolean keepAlive;
        /**
         * 是否在面包屑上隐藏
         */
        private Boolean breadcrumb;
        /**
         * 拥有菜单权限的角色编码集合
         */
        private List<String> roles;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean getHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        public Boolean getAffix() {
            return affix;
        }

        public void setAffix(Boolean affix) {
            this.affix = affix;
        }

        public Boolean getKeepAlive() {
            return keepAlive;
        }

        public void setKeepAlive(Boolean keepAlive) {
            this.keepAlive = keepAlive;
        }

        public Boolean getBreadcrumb() {
            return breadcrumb;
        }

        public void setBreadcrumb(Boolean breadcrumb) {
            this.breadcrumb = breadcrumb;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<RoutesVo> getChildren() {
        return children;
    }

    public void setChildren(List<RoutesVo> children) {
        this.children = children;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
