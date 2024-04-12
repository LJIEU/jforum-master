package com.liu.system.vo;

import lombok.Data;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/12 16:22
 */
@Data
public class DictDataVo {
    private Long id;
    private String name;
    private Integer sort;
    private Integer status;
    private String typeCode;
    private String value;
    private String css;
    private String theme;
    private String remark;
}
