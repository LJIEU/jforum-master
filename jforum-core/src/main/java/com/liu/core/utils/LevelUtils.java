package com.liu.core.utils;

import com.liu.core.converter.LevelConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 层次结构工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:46
 */
public class LevelUtils {

    /**
     * 构建 Level结构
     *
     * @param dList     需要转换的原始数据
     * @param converter 转换器
     * @param pid       父ID
     * @param <D>       原始数据类型
     * @param <L>       转换成的层次结构类型
     * @return 转换完成的层次类型 Level结构 列表
     */
    public static <D, L> List<L> buildTree(List<D> dList, LevelConverter<D, L> converter, Object pid) {
        // 转换 L类型 [虽然转换成 L类型 但是 lList没有层次结构需要的 pid 所以要依托 DList 去完成]
        List<L> lList = dList.stream().map(converter::dTtoL).collect(Collectors.toList());
        // 转换 Tree结构
        return dList.stream()
                // 过滤出顶层数据
                .filter(v -> converter.getPid(v).equals(pid))
                .map(v -> {
                    // 整理每一层的 Level结构 然后返回组成 集合列表 ==> [1,2,3,4] ==> [1对应一个层次 2对应一个层次...]
                    return deep(dList, v, lList,
                            converter.dTtoL(v), converter);
                }).collect(Collectors.toList());
    }

    /**
     * 深度遍历
     *
     * @param dList     原始数据
     * @param parent    原始数据的父数据
     * @param lList     构建集合 缓存前数据
     * @param parentL   层次结构的父数据
     * @param converter 转换器
     * @param <D>       原始数据类型
     * @param <L>       层次结构数据类型
     * @return 整理好的 层次结构数据
     */
    private static <D, L> L deep(List<D> dList, D parent,
                                 List<L> lList, L parentL, LevelConverter<D, L> converter) {
        int i = 0;
        for (D d : dList) {
            if (d != parent && converter.getId(parent).equals(converter.getPid(d))) {
                deep(dList, d, lList, lList.get(i), converter);
                converter.getChildrenByL(parentL).add(lList.get(i));
            }
            i++;
        }
        return parentL;
    }

}
