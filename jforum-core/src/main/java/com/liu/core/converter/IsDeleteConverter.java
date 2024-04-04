package com.liu.core.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * Description: 逻辑删除转换器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/01 13:56
 */
public class IsDeleteConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 转Java类型  ==> 读取Excel时会调用该方法
     */
    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String stringValue = context.getReadCellData().getStringValue();
        if (stringValue.equals("已删除")) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 转Excel类型  写入Excel时会调用
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        return new WriteCellData<>(context.getValue() == 1 ? "已删除" : "未删除");
    }
}
