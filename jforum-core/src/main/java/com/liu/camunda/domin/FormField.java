package com.liu.camunda.domin;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/02 21:25
 */
public class FormField implements Serializable {
    @Serial
    private static final long serialVersionUID = -3801599904205957384L;
    private String id;
    private String label;
    private String type;

    @Override
    public String toString() {
        return "FormField{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
