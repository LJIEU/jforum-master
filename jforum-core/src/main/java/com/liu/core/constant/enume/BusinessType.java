package com.liu.core.constant.enume;

/**
 * Description: 功能类型 通过 ordinal() 来获取对应的位置
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:47
 */
public enum BusinessType {
    /**
     * 其他 0
     */
    OTHER(0, "其他"),
    /**
     * 创建 1
     */
    CREATE(1, "创建"),
    /**
     * 查询 2
     */
    SELECT(2, "查询"),
    /**
     * 修改 3
     */
    UPDATE(3, "修改"),
    /**
     * 删除 4
     */
    DELETE(4, "删除");

    private final Integer value;
    private final String label;

    BusinessType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }
}
