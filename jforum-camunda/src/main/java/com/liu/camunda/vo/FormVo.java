package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Description: 表单数据处理
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/30 21:11
 */
@Schema(name = "表单数据处理")
public class FormVo {
    /**
     * 字段信息
     */
    private String label;
    /**
     * 字段
     */
    private String prop;
    /**
     * 字段类型 input select radio checkbox
     */
    private String type;
    /**
     * 下拉列表
     */
    private List<MyOption> options;

    /**
     * 是否是文件上传字段
     */
    private boolean isFileUpload;

    static class MyOption {
        private String label;
        private String value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MyOption> getOptions() {
        return options;
    }

    public void setOptions(List<MyOption> options) {
        this.options = options;
    }
}
