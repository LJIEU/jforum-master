package com.liu.core.converter;

import java.util.List;

/**
 * Description: 层次结构
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:48
 */
public interface LevelConverter<D, L> {
    /**
     * 获取 父ID
     *
     * @param v D 类型
     */
    Object getPid(D v);

    /**
     * D 数据类型 转 L 数据
     *
     * @param parent 父D类型
     * @return 返回 转换后 的类型L
     */
    L dTtoL(D parent);

    /**
     * 获取 ID
     *
     * @param parent D类型
     */
    Object getId(D parent);

    /**
     * 获取 L类型 的子类
     *
     * @param parent L类型
     * @return L的孩子节点
     */
    List<L> getChildrenByL(L parent);
}
