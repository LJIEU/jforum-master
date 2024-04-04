package com.liu.core.utils;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 分页工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 9:52
 */
public class PageUtils extends PageHelper {

    public static String orderBy(String sortRules, Boolean isDesc) {
        // 返回 排序规则 如:goods_id desc
        StringBuilder sb = new StringBuilder(sortRules.length());
        if (StringUtils.isNoneBlank()) {
            sb.append(sortRules);
        }
        if (isDesc) {
            sb.append(" " + "desc");
        }
        // 默认就是 正序:asc 不用赋值操作
        return sb.toString();
    }

    /**
     * 清理
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
