package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:18
 */
@Schema(name = "部署信息")
public class DeployVo {

    /**
     * 名称
     */
    @Schema(description = "Bpmn流程图名称")
    private String bpmnName;

    /**
     * 需要部署的bpmn路径
     */
    @Schema(description = "Bpmn流程图路径")
    private String resourcePath;

    public String getBpmnName() {
        return bpmnName;
    }

    public void setBpmnName(String bpmnName) {
        this.bpmnName = bpmnName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
