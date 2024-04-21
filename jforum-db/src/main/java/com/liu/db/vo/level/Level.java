package com.liu.db.vo.level;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 18:26
 */
@Data
public class Level {
    private Long value;
    private String label;
    private List<Level> children;
}
