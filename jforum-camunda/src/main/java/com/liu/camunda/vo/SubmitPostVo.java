package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/28 22:16
 */
@Schema(name = "提交审批")
public class SubmitPostVo {
    @Schema(description = "流程实例")
    String processInstanceId;

    @Schema(description = "意见")
    String opinion;


    @Schema(description = "选项")
    String options;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
