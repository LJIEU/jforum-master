package com.liu.db.vo.level;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 18:26
 */
public class Level {
    private Object value;
    private String label;
    private List<Level> children;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Level> getChildren() {
        return children;
    }

    public void setChildren(List<Level> children) {
        this.children = children;
    }
}
