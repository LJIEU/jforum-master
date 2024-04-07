package com.liu.system.vo;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 22:14
 */
@Data
public class RoutesVo {
    private Long id;
    private Long pid;
    private String name;
    private String path;
    private String component;
    private List<RoutesVo> children;

    private Meta meta;

    @Data
    public static class Meta {
        /**
         * 菜单名称
         */
        private String title;
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 菜单是否隐藏
         */
        private Boolean hidden;
        /**
         * 是否固定页签
         */
        private Boolean affix;
        /**
         * 是否缓存页面
         */
        private Boolean keepAlive;
        /**
         * 是否在面包屑上隐藏
         */
        private Boolean breadcrumb;
        /**
         * 拥有菜单权限的角色编码集合
         */
        private List<String> roles;
    }
}
