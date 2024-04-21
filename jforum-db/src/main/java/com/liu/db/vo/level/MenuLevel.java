package com.liu.db.vo.level;

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
    /**
     * 不可选择 true不可选  false可选
     */
    private Boolean disabled;
}
