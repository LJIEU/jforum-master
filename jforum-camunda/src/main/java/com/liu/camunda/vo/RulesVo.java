package com.liu.camunda.vo;

/**
 * Description: 表单校验规则
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/01 15:24
 */
public class RulesVo {
    private String field;
    private Boolean required;
    private String message;
    /**
     * blur[鼠标失去焦点触发校验] 和 change[内容数据改变触发校验]
     */
    private String trigger;
    /**
     * 一般都是默认string  但是如复选框就是 array
     */
    private String type;


    @Override
    public String toString() {
        return "RulesVo{" +
                "field='" + field + '\'' +
                ", required=" + required +
                ", message='" + message + '\'' +
                ", trigger='" + trigger + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
