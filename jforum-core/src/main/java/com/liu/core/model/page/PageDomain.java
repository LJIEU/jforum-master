//package com.liu.core.model.page;
//
//import cn.hutool.core.util.StrUtil;
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * Description: 分页数据
// *
// * @author 杰
// * @version 1.0
// * @since 2024/03/30 17:26
// */
//@Data
//public class PageDomain {
//
//    /**
//     * 当前记录起始索引
//     */
//    private Integer pageNum;
//
//    /**
//     * 每页显示记录数
//     */
//    private Integer pageSize;
//
//    /**
//     * 排序列
//     */
//    private String orderByColumn;
//
//    /**
//     * 排序的方向desc或者asc
//     */
//    private String isAsc = "asc";
//
//    /**
//     * 分页参数合理化
//     */
//    private Boolean reasonable = true;
//
//    public String getOrderBy() {
//        if (StringUtils.isEmpty(orderByColumn)) {
//            return "";
//        }
//        // 转驼峰命名
//        return StrUtil.toCamelCase(orderByColumn) + " " + isAsc;
//    }
//
//}
