package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 14:10
 */
@Schema(name = "完成任务")
public class CompleteTaskVo {

    @Schema(description = "流程实例ID")
    @NotBlank(message = "流程实例id不能为空")
    private String processInstanceId;

    @Schema(description = "任务ID")
    @NotBlank(message = "任务id不能为空")
    private String taskId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
