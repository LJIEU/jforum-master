package com.liu.system.config.excel.handler;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysDept;
import com.liu.system.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 写入 拦截器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/14 17:29
 */
@Slf4j
public class UserWriteHandler extends AbstractColumnWidthStyleStrategy implements SheetWriteHandler {

    private SysDeptService deptService = SpringUtils.getBean(SysDeptService.class);

    /**
     * 在创建 Sheet 工作表 之后执行
     *
     * @param context 上下文
     */
    @Override
    public void afterSheetCreate(SheetWriteHandlerContext context) {
        log.info("{}写入成功~", context.getWriteSheetHolder().getSheetNo());
        // 区间设置第一列第一行和第二行的数据。由于第一行是头，所以第一、二行的数据实际上是第二三行
        // row 行   col 列  ==> 1,-1  1,2  ==> 就是 第1行第2列
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 10000, 1, 1);
        DataValidationHelper helper = context.getWriteSheetHolder().getSheet().getDataValidationHelper();

        // 获取所有部门
        List<SysDept> deptList = deptService.selectSysDeptList(null);
        String[] names = new String[deptList.size()];
        for (int i = 0; i < deptList.size(); i++) {
            names[i] = deptList.get(i).getDeptName();
        }
        DataValidationConstraint constraint = helper.createExplicitListConstraint(names);
        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
        context.getWriteSheetHolder().getSheet().addValidationData(dataValidation);

        // 性别
        context.getWriteSheetHolder().getSheet().addValidationData(
                helper.createValidation(helper.createExplicitListConstraint(new String[]{"男", "女", "未知"}),
                        new CellRangeAddressList(1, 10000, 7, 7))
        );
        // 状态
        context.getWriteSheetHolder().getSheet().addValidationData(
                helper.createValidation(helper.createExplicitListConstraint(new String[]{"正常", "停用"}),
                        new CellRangeAddressList(1, 10000, 9, 9))
        );
        // 删除
/*        context.getWriteSheetHolder().getSheet().addValidationData(
                helper.createValidation(helper.createExplicitListConstraint(new String[]{"未删除", "已删除"}),
                        new CellRangeAddressList(1, 10000, 17, 17))
        );*/
    }


    public static HorizontalCellStyleStrategy style() {
        // 方法1 使用已有的策略 推荐
        // HorizontalCellStyleStrategy 每一行的样式都一样 或者隔行一样
        // AbstractVerticalCellStyleStrategy 每一列的样式都一样 需要自己回调每一页
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为灰色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        WriteFont headWriteFont = new WriteFont();
        // 字体高度
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteFont.setFontName("宋体");
        headWriteFont.setBold(Boolean.TRUE);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 自适应宽度
        headWriteCellStyle.setShrinkToFit(Boolean.TRUE);
        // 使用换行符
        headWriteCellStyle.setWrapped(Boolean.TRUE);
        // 水平 垂直 居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置前缀  字符串格式 ==》 'abc
//        headWriteCellStyle.setQuotePrefix(Boolean.TRUE);

        // 内容的策略 =====================================
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景 黄色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
        // 内容的策略
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("微软雅黑");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteFont.setColor(IndexedColors.BLACK.getIndex());
        // 字体大小
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setWrapped(false);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setShrinkToFit(true);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    private final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer integer, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (needSetWidth) {
            Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>());

            Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
            // 单元格文本长度大于60换行
            if (columnWidth >= 0) {
                if (columnWidth > 80) {
                    columnWidth = 80;
                }
                Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                    maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                    Sheet sheet = writeSheetHolder.getSheet();
                    sheet.setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
                }
            }
        }
    }

    /**
     * 计算长度
     *
     * @param cellDataList 数据列表
     * @param cell
     * @param isHead       是否头
     * @return 返回长度
     */
    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData<?> cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        // 换行符（数据需要提前解析好）
                        int index = cellData.getStringValue().indexOf("\n");
                        return index != -1 ?
                                cellData.getStringValue().substring(0, index).getBytes().length + 1 : cellData.getStringValue().getBytes().length + 1;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length;
                    default:
                        return -1;
                }
            }
        }
    }
}
