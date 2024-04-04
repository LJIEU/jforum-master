package com.liu.core.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Description: Excel表格工具类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/31 15:51
 */
@Slf4j
public class ExcelUtil<T> {

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 将 list 数据 导入到 Excel 表单
     *
     * @param response                返回数据
     * @param list                    数据集合
     * @param sheetName               工作表名称
     * @param excludeColumnFiledNames 排除字段
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName, Set<String> excludeColumnFiledNames) {
        exportExcel(response, list, excludeColumnFiledNames, sheetName, sheetName
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
    }


    public void exportExcel(HttpServletResponse response, List<T> list, Set<String> excludeColumnFiledNames, String sheetName, String title) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(title, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), clazz).excludeColumnFieldNames(excludeColumnFiledNames).sheet(sheetName).doWrite(list);
        } catch (IOException e) {
            log.error("导出Excel异常{}", e.getMessage());
        }
    }
}
