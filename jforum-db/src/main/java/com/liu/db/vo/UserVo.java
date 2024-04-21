package com.liu.db.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 14:50
 */
@Getter
@Setter
public class UserVo {
    private Long id;
    private Long deptId;
    private String username;
    private String nickname;
    private String mobile;
    private Long gender;
    private String avatar;
    private String email;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private List<Long> roleIds;
}