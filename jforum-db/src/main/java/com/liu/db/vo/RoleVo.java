package com.liu.db.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/06 14:02
 */
@Data
public class RoleVo {
    private Long id;
    private String name;
    private String code;
    @Range(min = 0, max = 1, message = "状态只能为1或者0")
    private Integer status;
    private Integer sort;
    private String remark;
    @Range(min = 1, max = 4, message = "请选择范围内的选项")
    private Integer dataScope;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
