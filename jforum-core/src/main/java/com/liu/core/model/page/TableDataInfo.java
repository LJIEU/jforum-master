//package com.liu.core.model.page;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * Description: 表信息
// *
// * @author 杰
// * @version 1.0
// * @since 2024/03/30 17:04
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class TableDataInfo implements Serializable {
//    @Serial
//    private static final long serialVersionUID = -255299056827366290L;
//    /**
//     * 总记录数
//     */
//    private long total;
//
//    /**
//     * 列表数据
//     */
//    private List<?> rows;
//
//    /**
//     * 消息状态码
//     */
//    private int code;
//
//    /**
//     * 消息内容
//     */
//    private String msg;
//}