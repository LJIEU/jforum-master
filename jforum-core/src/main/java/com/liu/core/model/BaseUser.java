package com.liu.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 15:50
 */
@AllArgsConstructor
@Data
public class BaseUser {
    private Long userId;
    private String password;
    private String username;
    private String status;
}
