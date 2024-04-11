package com.liu.system.vo.level;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 17:34
 */
@Data
public class DeptLevel {
    private Long value;
    private String label;
    private List<DeptLevel> children;
}
