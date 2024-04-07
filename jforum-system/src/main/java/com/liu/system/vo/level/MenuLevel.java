package com.liu.system.vo.level;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:52
 */
@Data
public class MenuLevel {
    private Long value;
    private String label;
    private List<MenuLevel> children;
}
