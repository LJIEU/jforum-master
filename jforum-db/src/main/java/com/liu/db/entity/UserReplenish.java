package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 用户补充信息对象 user_replenish
 *
 * @author JIE
 * @since 2024-05-15
 */
@Schema(name = "用户补充信息--实体类")
public class UserReplenish extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @ExcelProperty(value = "用户名")
    private String userName;
    /**
     * 背景图
     */
    @Schema(description = "背景图")
    @ExcelProperty(value = "背景图")
    private String backgroundUrl;
    /**
     * 行业值 与字典对应
     */
    @Schema(description = "行业值 与字典对应")
    @ExcelProperty(value = "行业值 与字典对应")
    private String industryValue;
    /**
     * 个性签名
     */
    @Schema(description = "个性签名")
    @ExcelProperty(value = "个性签名")
    private String signature;
    /**
     * 地址值 多个用|分割开
     */
    @Schema(description = "地址值 多个用|分割开")
    @ExcelProperty(value = "地址值 多个用|分割开")
    private String addressArr;


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }


    public void setIndustryValue(String industryValue) {
        this.industryValue = industryValue;
    }

    public String getIndustryValue() {
        return industryValue;
    }


    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }


    public void setAddressArr(String addressArr) {
        this.addressArr = addressArr;
    }

    public String getAddressArr() {
        return addressArr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("backgroundUrl", getBackgroundUrl())
                .append("industryValue", getIndustryValue())
                .append("signature", getSignature())
                .append("addressArr", getAddressArr())
                .append("updateTime", getUpdateTime())
                .append("isDelete", getIsDelete())
                .toString();
    }
}