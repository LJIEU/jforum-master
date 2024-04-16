package com.liu.system.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/04 23:55
 */
@Accessors(chain = true)
@Data
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
}
