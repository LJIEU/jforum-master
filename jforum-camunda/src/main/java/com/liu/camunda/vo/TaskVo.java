package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description: 任务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 13:37
 */
@Schema(name = "任务参数")
public class TaskVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -7991132327744480771L;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "任务ID")
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
