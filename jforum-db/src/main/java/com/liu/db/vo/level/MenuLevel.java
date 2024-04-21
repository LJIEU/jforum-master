package com.liu.db.vo.level;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:52
 */
public class MenuLevel {
    private Long value;
    private String label;
    private List<MenuLevel> children;
    /**
     * 不可选择 true不可选  false可选
     */
    private Boolean disabled;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<MenuLevel> getChildren() {
        return children;
    }

    public void setChildren(List<MenuLevel> children) {
        this.children = children;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
