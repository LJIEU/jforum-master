package com.liu.generator.service;

import com.liu.generator.entity.GenTable;
import com.liu.generator.entity.GenTableColumn;
import com.liu.generator.mapper.GenTableColumnMapper;
import com.liu.generator.utils.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 17:20
 */
@Service
public class GenTableColumnService {

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 插入表 对应的字段信息
     *
     * @param genTable 生成的表信息
     * @return 返回字段集
     */
    @Transactional(rollbackFor = Exception.class)
    public List<GenTableColumn> insertColumn(GenTable genTable) {
        List<GenTableColumn> columns = genTableColumnMapper.getTableColumnsInfo(genTable.getTableName());
        for (GenTableColumn column : columns) {
            GenUtils.initColumn(column, genTable);
            // 将该信息写入表中
            Long id = genTableColumnMapper.insert(column);
//            String id = genTableColumnMapper.selectId(column.getCreateTime().getTime() / 1000L);
            // 设置添加后的ID
            column.setColumnId(id);
        }
        return columns;
    }

}
