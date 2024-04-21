package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 系统访问记录对象 sys_login_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "系统访问记录--实体类")
public class SysLoginLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 访问ID
     */
    @Schema(description = "访问ID")
    @ExcelProperty(value = "访问ID")
    private Long loginId;
    /**
     * 用户账号
     */
    @Schema(description = "用户账号")
    @ExcelProperty(value = "用户账号")
    private String userName;
    /**
     * 登录IP地址
     */
    @Schema(description = "登录IP地址")
    @ExcelProperty(value = "登录IP地址")
    private String ip;
    /**
     * 登录地点
     */
    @Schema(description = "登录地点")
    @ExcelProperty(value = "登录地点")
    private String loginLocation;
    /**
     * 浏览器类型
     */
    @Schema(description = "浏览器类型")
    @ExcelProperty(value = "浏览器类型")
    private String browser;
    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    @ExcelProperty(value = "操作系统")
    private String os;
    /**
     * 登录状态（0成功 1失败）
     */
    @Schema(description = "登录状态（0成功 1失败）")
    @ExcelProperty(value = "登录状态（0成功 1失败）")
    private String status;
    /**
     * 提示消息
     */
    @Schema(description = "提示消息")
    @ExcelProperty(value = "提示消息")
    private String message;


    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public Long getLoginId() {
        return loginId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }


    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getLoginLocation() {
        return loginLocation;
    }


    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowser() {
        return browser;
    }


    public void setOs(String os) {
        this.os = os;
    }

    public String getOs() {
        return os;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("loginId", getLoginId())
                .append("userName", getUserName())
                .append("ip", getIp())
                .append("loginLocation", getLoginLocation())
                .append("browser", getBrowser())
                .append("os", getOs())
                .append("status", getStatus())
                .append("message", getMessage())
                .append("createTime", getCreateTime())
                .toString();
    }
}