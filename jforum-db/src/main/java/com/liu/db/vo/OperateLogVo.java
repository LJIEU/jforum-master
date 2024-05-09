package com.liu.db.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;


/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/08 22:19
 */
@Schema(name = "操作日志前端")
public class OperateLogVo {
    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "模块")
    private String moduleName;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "描述")
    private String describe;

    @Schema(description = "耗时")
    private String costTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "操作类型")
    private Integer businessType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getBusinessType() {
        return businessType;
    }
}
