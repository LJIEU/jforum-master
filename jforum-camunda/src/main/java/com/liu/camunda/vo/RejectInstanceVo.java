package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 20:45
 */
@Schema(name = "驳回信息")
public class RejectInstanceVo {

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例ID")
    @NotBlank(message = "流程实例id不能为空")
    private String processInstanceId;

    /**
     * 目标节点id
     */
    @Schema(description = "目标节点ID不能为空")
    @NotBlank(message = "目标节点id不能为空")
    private String targetNodeId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }
}
