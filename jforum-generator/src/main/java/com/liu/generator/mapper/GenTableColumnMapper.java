package com.liu.generator.mapper;

import com.liu.generator.entity.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 19:18
 */
@Mapper
public interface GenTableColumnMapper {
    /**
     * 根据表ID 获取字段列表
     *
     * @param tableId 表ID
     * @return 返回字段列表
     */
    List<GenTableColumn> getColumnsByTableId(Long tableId);

    /**
     * 获取表字段信息
     *
     * @param tableName 表名称
     * @return 返回字段列表
     */
    List<GenTableColumn> getTableColumnsInfo(String tableName);

    /**
     * 插入数据
     *
     * @param genTableColumn 字段信息
     * @return 返回字段ID
     */
    Long insert(GenTableColumn genTableColumn);

    /**
     * 根据表名称 查询主键
     *
     * @param tableName 表名称
     */
    GenTableColumn selectPkByTableName(String tableName);
}
