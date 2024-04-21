package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Description: 顺序加签
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 19:09
 */
public class SequentialAddVo {
    @Schema(description = "流程实例ID")
    @NotBlank(message = "流程实例ID不能为空")
    private String processInstanceId;

    @Schema(description = "节点ID")
    @NotBlank(message = "节点ID不能为空")
    private String nodeId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
