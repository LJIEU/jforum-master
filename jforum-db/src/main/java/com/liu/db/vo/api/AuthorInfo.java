package com.liu.db.vo.api;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/11 16:54
 */
public class AuthorInfo {
    private String username;
    private String nickname;
    private String authorHome;
    private String avatarurl;
    private String signature;

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

    public String getAuthorHome() {
        return authorHome;
    }

    public void setAuthorHome(String authorHome) {
        this.authorHome = authorHome;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
