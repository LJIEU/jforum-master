package com.liu.db.vo;

import lombok.Data;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/12 17:40
 */
@Data
public class DeptVo {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    private Integer status;
}
