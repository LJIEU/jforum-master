package com.liu.system.vo;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/10 16:10
 */
@Data
public class MenuVo {
    private Long id;
    private Long parentId;
    private String name;
    private String type;
    private String path;
    private String component;
    private String perm;
    private Integer visible;
    private Integer sort;
    private String icon;
    private String redirect;
    private Integer keepAlive;
    private Integer alwaysShow;
    private String status;
    private String remark;
    private String routing;
    private List<MenuVo> children;
}
