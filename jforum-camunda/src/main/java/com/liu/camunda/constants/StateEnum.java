package com.liu.camunda.constants;

/**
 * Description: 审批状态和前端标签
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/27 21:08
 */
public enum StateEnum {

    /**
     * 审核通过
     */
    COMPLETE("审核通过", "success"),
    /**
     * 已批准
     */
    MY_COMPLETE("已批准", "success"),
    /**
     * 审核中
     */
    REVIEW("审核中", "primary"),
    /**
     * 待办
     */
    UPCOMING("待办流程", "warning"),
    /**
     * 驳回
     */
    REJECT("驳回", "danger"),
    /**
     * 被驳回
     */
    MY_REJECT("被驳回", "warning"),
    /**
     * 撤回
     */
    DELETE("撤回", "danger"),

    /**
     * 其他
     */
    OTHER("其他", "info");

    /**
     * 状态描述
     */
    private String state;
    /**
     * 前端标签类型
     */
    private String tagType;

    StateEnum(String state, String tagType) {
        this.state = state;
        this.tagType = tagType;
    }

    public String getState() {
        return state;
    }

    public String getTagType() {
        return tagType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
