package com.liu.system.config.excel.temple;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.liu.system.config.excel.converter.DeptExcelConverter;
import com.liu.system.config.excel.converter.SexExcelConverter;
import com.liu.system.config.excel.converter.StatusExcelConverter;
import com.liu.system.config.excel.converter.UserTypeExcelConverter;
import lombok.EqualsAndHashCode;

/**
 * Description: 用户数据模板
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/15 13:56
 */
@EqualsAndHashCode
public class UserTemple {

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private String userId;
    /**
     * 部门ID
     */
    @ColumnWidth(value = 14)
    @ExcelProperty(value = "部门", converter = DeptExcelConverter.class)
    private Long deptId;
    /**
     * 用户账号
     */
    @ExcelProperty(value = "用户账号")
    private String userName;
    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    private String nickName;
    /**
     * 用户类型（00系统用户）
     */
    @ColumnWidth(value = 17)
    @ExcelProperty(value = "用户类型\n（系统用户）", converter = UserTypeExcelConverter.class)
    private String userType;
    /**
     * 用户邮箱
     */
    @ExcelProperty(value = "用户邮箱")
    private String email;
    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）
     */
    @ExcelProperty(value = "用户性别", converter = SexExcelConverter.class)
    private String sex;
    /**
     * 头像地址
     */
    @ExcelProperty(value = "头像地址")
    private String avatar;
    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value = "帐号状态", converter = StatusExcelConverter.class)
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
