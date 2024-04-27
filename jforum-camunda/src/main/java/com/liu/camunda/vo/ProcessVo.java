package com.liu.camunda.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Description: 基础的流程信息
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/27 17:01
 */
@Schema(name = "基础的流程信息")
public class ProcessVo {
    @Schema(description = "ID")
    private String id;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程名称")
    private String name;

    @Schema(description = "业务Key")
    private String businessKey;

    @Schema(description = "审批状态")
    private String state;

    @Schema(description = "返回前端的标签类型:success|primary|warning|danger")
    private String tagType;

    @Schema(description = "是否我发起的")
    private Boolean isMyInitiate;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getMyInitiate() {
        return isMyInitiate;
    }

    public void setMyInitiate(Boolean myInitiate) {
        isMyInitiate = myInitiate;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
