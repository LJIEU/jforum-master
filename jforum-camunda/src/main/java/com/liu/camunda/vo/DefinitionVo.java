package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 18:04
 */
@Schema(name = "流程定义")
public class DefinitionVo {

    /**
     * 流程定义key
     */
    @Schema(description = "流程定义Key")
    @NotBlank(message = "流程定义key不能为空")
    private String processDefinitionKey;

    /**
     * 流程定义数据
     */
    @Schema(description = "流程定义数据")
    @NotEmpty(message = "流程定义不能为空")
    private String processDefinition;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(String processDefinition) {
        this.processDefinition = processDefinition;
    }
}
