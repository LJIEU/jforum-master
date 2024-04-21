package com.liu.db.converter.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * Description: 用户类型
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/15 17:26
 */
public class UserTypeExcelConverter implements Converter<String> {
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return "系统用户".equals(context.getReadCellData().getStringValue()) ? "00" : "01";
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        if (context.getValue().equals("temple")) {
            return new WriteCellData<>("用户类型 请根据提示填写!");
        }
        return new WriteCellData<>("00".equals(context.getValue()) ? "系统用户" : "普通用户");
    }
}
