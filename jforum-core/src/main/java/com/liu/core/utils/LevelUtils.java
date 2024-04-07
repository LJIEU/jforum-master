package com.liu.core.utils;

import com.liu.core.converter.LevelConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 层次结构工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:46
 */
@Deprecated(since = "好像有一些问题暂未解决")
public class LevelUtils<D, L> {
    private final List<D> dList;
    private final List<L> lList = new ArrayList<>();

    public LevelUtils(List<D> dList) {
        this.dList = dList;
    }

    /**
     * 构建 结构
     */
    public List<L> buildTree(List<D> list, LevelConverter<D, L> converter) {
        // 筛选出 根节点
        List<D> roots = list.stream().filter(v -> (Long) converter.getPid(v) == 0L).collect(Collectors.toList());
        // 遍历获取子节点
        roots.forEach(root -> buildTreeRecursively(root, lList, converter, dList));
        return lList;
    }


    /**
     * 递归构建 树级结构
     *
     * @param parent 父用户
     * @param tree   层次结构 Level
     */
    private void buildTreeRecursively(D parent, List<L> tree, LevelConverter<D, L> converter, List<D> dList) {
        L parentLevel = converter.dTtoL(parent);
        tree.add(parentLevel);
        dList.stream()
                .filter(user -> !user.equals(parent) && converter.getId(parent).equals(converter.getPid(user)))
                .forEach(child -> {
                    List<L> childrenByL = converter.getChildrenByL(parentLevel);
                    if (childrenByL == null) {
                        childrenByL = new ArrayList<>();
                        converter.setChildrenByL(parentLevel, childrenByL);
                    }
                    buildTreeRecursively(child, childrenByL, converter, dList);
                });
    }
}
