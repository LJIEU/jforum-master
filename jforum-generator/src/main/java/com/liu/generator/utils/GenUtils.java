package com.liu.generator.utils;

import com.liu.generator.entity.GenTable;
import com.liu.generator.entity.GenTableColumn;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Description: 代码生成工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/31 15:06
 */
public class GenUtils {
    /**
     * 设置字段 对应的信息 ==================
     *
     * @param column   字段
     * @param genTable 待代码生成的表
     *                 通过引用传递改变 column 值
     */
    public static void initColumn(GenTableColumn column, GenTable genTable) {
        // 所属表
        column.setTableId(genTable.getTableId());

        // 设置Java字段名 驼峰 goods_id -> goodsId
        String javaField = getColumnName(column.getColumnName().toLowerCase(Locale.ROOT)).toString();
        // goodsId
        column.setJavaField(javaField);
        // 设置Java类型 [默认字符串]
        column.setJavaType("String");
        String columnType = column.getColumnType();
        if (Stream.of("datetime", "time").anyMatch(columnType::contains)) {
            // 时间类型
            column.setJavaType("Date");
            // 需要设置时间格式
        } else if (Stream.of("tinyint", "bigint", "int", "decimal", "number").anyMatch(columnType::contains)) {
            // 如果是浮点型 统一使用 BigDecimal
            if (columnType.contains(",")) {
                column.setJavaType("BigDecimal");
            } else if (Integer.parseInt(StringUtils.substringBetween(columnType, "(", ")")) <= 10) {
                column.setJavaType("Integer");
            } else {
                column.setJavaType("Long");
            }
        }
        // 插入字段（默认所有字段都需要插入）
        column.setIsInsert(GenConstants.REQUIRE);

        String columnName = column.getColumnName();
        boolean isNotPk = !strToBool(column.getIsPk());
        // 编辑字段 确保不是主键 并且该字段不在列表内存在
        if (arraysNotContains(GenConstants.COLUMN_NOT_EDIT, columnName) && isNotPk) {
            column.setIsEdit(GenConstants.REQUIRE);
        }
        // 列表字段
        if (arraysNotContains(GenConstants.COLUMN_NOT_LIST, columnName) && isNotPk) {
            column.setIsList(GenConstants.REQUIRE);
        }
        // 查询字段
        if (arraysNotContains(GenConstants.COLUMN_NOT_QUERY, columnName) && isNotPk) {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        // 查询字段类型[如果是字符串就是like 否则 就是 eq]
//        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
//            column.setQueryType(GenConstants.QUERY_LIKE);
//        }
        if (column.getJavaType().equals("String")) {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }

        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            column.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex")) {
            column.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
            column.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
            column.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            column.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }

    /**
     * 字符串 与 Bool 转换
     */
    public static Boolean strToBool(String value) {
        return value.equals("1");
    }


    /**
     * 修饰 字段名称 驼峰 table_id ==> tableId
     *
     * @param columnName 字段名称
     * @return 返回驼峰字段名
     */
    public static StringBuilder getColumnName(String columnName) {
        StringBuilder sb = new StringBuilder(columnName.length());
        String[] s = columnName.split("_");
        for (int i = 0; i < s.length; i++) {
            // 第一截小写
            if (i == 0) {
                sb.append(s[i]);
            } else {
                // 后面首字母大写
                sb.append(s[i].substring(0, 1).toUpperCase(Locale.ROOT))
                        .append(s[i].substring(1));
            }
        }
        return sb;
    }


    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysNotContains(String[] arr, String targetValue) {
        return !Arrays.asList(arr).contains(targetValue);
    }
}
