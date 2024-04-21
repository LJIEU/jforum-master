package com.liu.db.converter.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysDictData;
import com.liu.db.service.SysDictDataService;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/14 16:00
 */
public class SexExcelConverter implements Converter<String> {
    /*
        @Override
        public Class<?> supportJavaTypeKey() {
            return String.class;
        }
    */

    private final SysDictDataService dictDataService = SpringUtils.getBean(SysDictDataService.class);

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String sexLabel = context.getReadCellData().getStringValue();
        // 查询字典 转 类型
        SysDictData select = new SysDictData();
        select.setDictType("sys_user_sex");
        List<SysDictData> dictData = dictDataService.selectSysDictDataList(select);
        AtomicReference<String> sexValue = new AtomicReference<>("");
        dictData.forEach(v -> {
            if (v.getDictLabel().equals(sexLabel)) {
                sexValue.set(v.getDictValue());
            }
        });
        return sexValue.get();
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        String sexValue = context.getValue();
        if (sexValue.equals("temple")) {
            return new WriteCellData<>("性别 请根据提示填写!");
        }
        // 查询字典 转 文字
        SysDictData select = new SysDictData();
        select.setDictType("sys_user_sex");
        AtomicReference<String> sexLabel = new AtomicReference<>("xx");
        List<SysDictData> dictData = dictDataService.selectSysDictDataList(select);
        dictData.forEach(v -> {
            if (v.getDictValue().equals(sexValue)) {
                sexLabel.set(v.getDictLabel());
            }
        });
        return new WriteCellData<>(sexLabel.get());
    }
}
