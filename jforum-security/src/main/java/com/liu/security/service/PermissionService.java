package com.liu.security.service;

import com.liu.core.constant.Constants;
import com.liu.core.model.LoginUser;
import com.liu.security.context.PermissionContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * Description: 自定义权限 不再使用 hasAuthority --》 这个就是使用了 getAuthorities() 方法收集的权限
 * 判断是否包含 hasAuthority 中的值
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:38
 */
@Service("authority")
public class PermissionService {

    /**
     * 验证用户是否具备某权限
     */
    public boolean hasPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        // 获取用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermission(loginUser.getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     */
    private boolean hasPermission(Set<String> permissions, String permission) {
        return permissions.contains(Constants.ALL_PERMISSION) ||
                permissions.contains(permission);
    }

}
