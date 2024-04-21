package com.liu.core.model;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:50
 */
public class BaseUser {
    private Long userId;
    private String password;
    private String username;
    private String status;

    public BaseUser() {
    }

    public BaseUser(Long userId, String password, String username, String status) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
