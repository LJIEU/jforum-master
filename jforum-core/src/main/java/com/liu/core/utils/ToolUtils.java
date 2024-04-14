package com.liu.core.utils;

import com.liu.core.constant.enume.ToolEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 自定义的工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/13 11:00
 */
public class ToolUtils {
    public static Map<ToolEnum, Object> multiple(List<Long> dbIds, List<Long> requireIds) {
        int minLength = Math.min(dbIds.size(), requireIds.size());
        HashMap<ToolEnum, Object> map = new HashMap<>(3);
        HashMap<Long, Long> updateMap = new HashMap<>(1);
        List<Long> deleteList = new ArrayList<>();
        List<Long> addList = new ArrayList<>();
        for (int i = 0; i < minLength; i++) {
            if (!dbIds.get(i).equals(requireIds.get(i))) {
                updateMap.put(dbIds.get(i), requireIds.get(i));
//                System.out.println("修改:" + dbIds.get(i) + "--->" + requireIds.get(i));
            }
        }

        // 处理多余的元素
        for (int i = minLength; i < dbIds.size(); i++) {
            deleteList.add(dbIds.get(i));
//            System.out.println("删除:" + dbIds.get(i));
        }

        // 处理缺失的元素
        for (int i = minLength; i < requireIds.size(); i++) {
            addList.add(requireIds.get(i));
//            System.out.println("添加:" + requireIds.get(i));
        }
        map.put(ToolEnum.UPDATE, updateMap);
        map.put(ToolEnum.ADD, addList);
        map.put(ToolEnum.DELETE, deleteList);
        return map;
    }
}
