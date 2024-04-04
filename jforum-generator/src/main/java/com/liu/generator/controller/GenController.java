package com.liu.generator.controller;

import cn.hutool.json.JSONUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.generator.entity.GenTable;
import com.liu.generator.service.GenTableColumnService;
import com.liu.generator.service.GenTableService;
import com.liu.generator.vo.GenTableVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 17:01
 */
@Tag(name = "代码生成")
@RequestMapping("/tools/gen")
@RestController
public class GenController extends BaseController {

    @Autowired
    private GenTableService genTableService;

    @Autowired
    private GenTableColumnService genTableColumnService;

    /**
     * 预览代码
     */
    @Operation(summary = "预览代码")
    @Parameters({
            @Parameter(name = "id", description = "待代码生成的表ID", in = ParameterIn.PATH)
    })
    @GetMapping("/preview/{ids}")
    public R<String> preview(@PathVariable("ids") String[] ids) {
        Map<String, String> dataMap = genTableService.previewCode(ids);
        String s = JSONUtil.toJsonStr(dataMap);
        return R.success(s);
    }

    /**
     * 将需要代码生成的表 导入数据库
     */
    @Operation(summary = "导入待生成的表")
//    @Parameters({
//            @Parameter(name = "tableName", description = "表名称", in = ParameterIn.PATH),
//            @Parameter(name = "functionName", description = "表功能描述", in = ParameterIn.QUERY),
//            @Parameter(name = "template", description = "模板选择[crud,tree,sub]", in = ParameterIn.QUERY),
//            @Parameter(name = "sub", description = "关联", in = ParameterIn.QUERY)
//    })
    @GetMapping("/importInfo/{tableName}")
    public R<GenTable> importInfo(@Valid GenTableVo genTableVo) {
        // 表信息
        GenTable genTable = genTableService.insertInfo(genTableVo);
        // 字段信息
        genTable.setColumns(genTableColumnService.insertColumn(genTable));
        return R.success(genTable);
    }


    /**
     * 下载代码
     */
    @Operation(summary = "下载代码ZIP")
    @SneakyThrows
    @GetMapping("/download/{ids}")
    public void download(HttpServletResponse response, @PathVariable("ids") String[] ids) {
        byte[] data = genTableService.downloadCode(ids);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("代码生成.zip", StandardCharsets.UTF_8));
        response.addHeader("Content-Length", "" + data.length);
        // zip 格式
        response.setContentType("application/octet-stream;charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }


}
