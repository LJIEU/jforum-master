package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description: 流程定义参数
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:58
 */
@Schema(name = "流程定义")
public class StartProcessVo {
    /**
     * 流程定义key
     */
    @Schema(description = "流程定义Key")
    private String processDefinitionKey;
    /**
     * 流程定义id
     */
    @Schema(description = "流程定义id")
    private String processDefinitionId;
    /**
     * 业务key
     */
    @Schema(description = "业务key")
    private String businessKey;
    /**
     * 发起人
     */
    @Schema(description = "发起人")
    private String initiator;
    /**
     * 租户id
     */
    @Schema(description = "租户id")
    private String tenantId;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
