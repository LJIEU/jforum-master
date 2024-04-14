package com.liu.system.config.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysDept;
import com.liu.system.service.SysDeptService;

/**
 * Description: 部门
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/14 15:38
 */
public class DeptExcelConverter implements Converter<Long> {

    private final SysDeptService deptService = SpringUtils.getBean(SysDeptService.class);


    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Long convertToJavaData(ReadConverterContext<?> context) throws Exception {
        // 部门名称
        String deptName = context.getReadCellData().getStringValue();
        // 去查询对应的 部门ID
        return deptService.selectSysDeptByDeptName(deptName);
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Long> context) throws Exception {
        // 查询 部门名称
        SysDept sysDept = deptService.selectSysDeptByDeptId(context.getValue());
        String deptName = "部门不存在!";
        if (sysDept != null) {
            deptName = sysDept.getDeptName();
        }
        return new WriteCellData<>(deptName);
    }
}
