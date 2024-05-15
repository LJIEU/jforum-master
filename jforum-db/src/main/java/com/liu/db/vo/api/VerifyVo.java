package com.liu.db.vo.api;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/14 11:17
 */
public class VerifyVo {
    private String jwtToken;
    private String username;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
