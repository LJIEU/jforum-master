package com.liu.camunda.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.Objects;

/**
 * Description: 流程节点信息
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/28 14:34
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Schema(name = "流程节点信息")
public class HistVo {

    @Schema(description = "任务ID")
    private String tackId;

    @Schema(description = "当前节点名称")
    private String name;

    @Schema(description = "最后执行者")
    private String lastUser;

    @Schema(description = "状态[0完成  1进行中]")
    Integer state;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    Date startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    Date endTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistVo histVo)) return false;
        return Objects.equals(getTackId(), histVo.getTackId()) && Objects.equals(getName(), histVo.getName()) && Objects.equals(getLastUser(), histVo.getLastUser()) && Objects.equals(getState(), histVo.getState()) && Objects.equals(getStartTime(), histVo.getStartTime()) && Objects.equals(getEndTime(), histVo.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTackId(), getName(), getLastUser(), getState(), getStartTime(), getEndTime());
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTackId() {
        return tackId;
    }

    public void setTackId(String tackId) {
        this.tackId = tackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
