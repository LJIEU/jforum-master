package com.liu.generator.mapper;

import com.liu.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 17:43
 */
@Mapper
public interface GenTableMapper {
    /**
     * 获取 选择生成的表信息
     *
     * @param tableName 表名
     * @return 返回表基础信息
     */
    GenTable getTableInfo(@Param("tableName") String tableName);

    /**
     * 插入表信息
     *
     * @param genTable 表信息
     * @return 返回 代码生成表的ID
     */
    Long insertGenTable(GenTable genTable);


    /**
     * 查询 代码生成表信息
     *
     * @param id 代码生成表ID
     * @return 返回表信息
     */
    GenTable selectTableById(String id);

    String selectTableByName(String subName);
}
