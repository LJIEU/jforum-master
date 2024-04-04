package com.liu.generator.utils;

/**
 * Description: 常量
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/31 15:10
 */
public interface GenConstants {
    /**
     * 要求 ==》 是1
     */
    String REQUIRE = "1";
    /**
     * 不可编辑字段
     */
    String[] COLUMN_NOT_EDIT = {"id", "create_by", "create_time", "is_delete"};
    /**
     * 页面不需要显示的字段列表
     */
    String[] COLUMN_NOT_LIST = {"id", "create_by", "create_time", "is_delete", "update_by",
            "update_time"};
    /**
     * 页面不需要查询的字段
     */
    String[] COLUMN_NOT_QUERY = {"id", "create_by", "create_time", "is_delete", "update_by",
            "update_time", "remark"};

    /**
     * 页面单选框
     */
    String HTML_RADIO = "radio";

    /**
     * 文本框
     */
    String HTML_INPUT = "input";

    /**
     * 文本域
     */
    String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    String HTML_SELECT = "select";


    /**
     * 复选框
     */
    String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件
     */
    String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    String HTML_EDITOR = "editor";

    /**
     * 模糊查询
     */
    String QUERY_LIKE = "LIKE";
}
