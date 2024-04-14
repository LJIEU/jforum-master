package com.liu.system.config.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/14 16:26
 */
public class StatusExcelConverter implements Converter<String> {

    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return "正常".equals(context.getReadCellData().getStringValue()) ? "0" : "1";
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        return new WriteCellData<>("0".equals(context.getValue()) ? "正常" : "停用");
    }
}
