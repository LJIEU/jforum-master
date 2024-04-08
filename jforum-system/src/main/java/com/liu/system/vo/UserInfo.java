package com.liu.system.vo;

import com.liu.system.dao.SysRole;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
    private String loginTenantId;

    private Long userId;
    private String username;
    private String nickname;
    private Long deptId;
    private String avatar;
    private String homePath;
    private String tenantId;
    private List<SysRole> roleInfo;
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
