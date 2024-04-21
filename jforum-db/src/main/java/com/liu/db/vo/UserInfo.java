package com.liu.db.vo;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 23:55
 */
public class UserInfo {
    private Long userId;
    private String username;
    private String nickname;
    private Long deptId;
    private String avatar;
    private String currRole;
    private List<String> roles;
    private Set<String> perms;
    /*链式编程 和 构造器原理*/
/*

    public UserInfo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public static UserInfoBuilder build() {
        return new UserInfoBuilder();
    }

    public static class UserInfoBuilder {
        private String userId;

        public UserInfoBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }
    }

*/


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCurrRole() {
        return currRole;
    }

    public void setCurrRole(String currRole) {
        this.currRole = currRole;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPerms() {
        return perms;
    }

    public void setPerms(Set<String> perms) {
        this.perms = perms;
    }
}
