package com.liu.core.utils;

import com.liu.core.converter.TreeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 树型结构
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 9:54
 */
public class TreeUtils {
    /**
     * 转换为 Tree 结构
     */
    public static <D> List<D> convertTree(List<D> list, TreeConverter<D> converter) {
        return list.stream()
                .filter(v -> converter.getPid(v) == 0)
                .peek(v -> {
                    deep(list, v, converter);
                })
                .collect(Collectors.toList());
    }

    /**
     * 递归获取 ==》 组成Tree结构
     *
     * @param list   列表
     * @param parent 父节点
     */
    public static <D> void deep(List<D> list, D parent, TreeConverter<D> converter) {
        List<D> children = new ArrayList<>();
        for (D curr : list) {
            // 说明该元素是子元素
            if (!curr.equals(parent) && converter.getPid(curr).equals(converter.getId(parent))) {
                if (converter.getChildren(parent) == null) {
                    deep(list, curr, converter);
                    children.add(curr);
                }
            }
        }
        converter.setChildren(parent, children);
    }
}
