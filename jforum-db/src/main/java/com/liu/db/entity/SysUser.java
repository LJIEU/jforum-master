package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liu.core.model.BaseEntity;
import com.liu.db.converter.excel.DeptExcelConverter;
import com.liu.db.converter.excel.SexExcelConverter;
import com.liu.db.converter.excel.StatusExcelConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.Date;

/**
 * 用户信息对象 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "用户信息--实体类")
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6913407805862215597L;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;
    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    @ColumnWidth(value = 14)
    @ExcelProperty(value = "部门", converter = DeptExcelConverter.class)
    private Long deptId;
    /**
     * 用户账号
     */
    @Schema(description = "用户账号")
    @ExcelProperty(value = "用户账号")
    private String userName;
    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    @ExcelProperty(value = "用户昵称")
    private String nickName;
    /**
     * 用户类型（00系统用户）
     */
    @Schema(description = "用户类型（00系统用户）")
    @ColumnWidth(value = 17)
    @ExcelProperty(value = "用户类型（00系统用户）\n")
    private String userType;
    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    @ExcelProperty(value = "用户邮箱")
    private String email;
    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    @ExcelProperty(value = "手机号码")
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @Schema(description = "用户性别（0男 1女 2未知  实际情况根据数据字典来定）")
    @ExcelProperty(value = "用户性别", converter = SexExcelConverter.class)
    private String sex;
    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    @ExcelProperty(value = "头像地址")
    private String avatar;
    /**
     * 密码
     */
    @Schema(description = "密码")
    @ExcelProperty(value = "密码")
    @ExcelIgnore
    private String password;
    /**
     * 帐号状态（0正常 1停用）
     */
    @Schema(description = "帐号状态（0正常 1停用）")
    @ExcelProperty(value = "帐号状态", converter = StatusExcelConverter.class)
    private String status;
    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP")
    @ExcelProperty(value = "最后登录IP")
    private String loginIp;
    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    @ExcelProperty(value = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ColumnWidth(20)
    private Date loginDate;


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginIp() {
        return loginIp;
    }


    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("userType", getUserType())
                .append("email", getEmail())
                .append("phone", getPhone())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("status", getStatus())
                .append("isDelete", getIsDelete())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}