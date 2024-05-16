//package com.liu.db.controller;
//
//import com.liu.db.core.controller.BaseController;
//import com.liu.db.core.converter.TreeConverter;
//import com.liu.db.core.result.R;
//import com.liu.db.core.utils.ExcelUtil;
//import com.liu.db.core.utils.TreeUtils;
//import com.liu.db.dao.SensitiveWord;
//import com.liu.db.service.SensitiveWordService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//import java.util.List;
//
///**
// * 敏感词控制层 sensitive_word
// *
// * @author JIE
// * @since 2024-05-16
// */
//@Tag(name = "屏蔽词")
//@RestController
//@RequestMapping("/system/sensitiveword")
//public class SensitiveWordController extends BaseController {
//
//    @Autowired
//    private SensitiveWordService sensitivewordService;
//
//    /**
//     * 查询 敏感词 列表
//     *
//     * @param pageNum   当前页码
//     * @param pageSize  页记录数
//     * @param sortRules 排序规则
//     * @param isDesc    是否逆序
//     * @param sensitiveword  敏感词对象
//     * @return 返回 分页 查询结果
//     */
//    @Operation(summary = "分页查询")
//    @Parameters({
//            @Parameter(name = "pageNum", description = "当前页", example = "1"),
//            @Parameter(name = "pageSize", description = "页大小", example = "10"),
//            @Parameter(name = "sortRules", description = "排序规则"),
//            @Parameter(name = "isDesc", description = "是否逆序排序"),
//            @Parameter(name = "sensitiveword", description = "实体参数")
//    })
//    @GetMapping("/list")
//    public R<List<SensitiveWord>> list(
//            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//            @RequestParam(value = "sortRules", defaultValue = "id") String sortRules,
//            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
//        SensitiveWord sensitiveword) {
//        startPage(pageNum, pageSize, sortRules, isDesc);
//        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
//        List<SensitiveWord> list = sensitivewordService.selectSensitiveWordList(sensitiveword);
//        clearPage();
//        return R.success(list);
//    }
//
//
//    /**
//     * 导出数据 Excel格式
//     */
//    @Operation(summary = "导出数据 Excel格式")
//    @GetMapping("/export")
//    public void export(HttpServletResponse response, SensitiveWord sensitiveword) {
//        // 忽略字段
//        Set<String> excludeColumnFiledNames = new HashSet<>();
//            excludeColumnFiledNames.add("createBy");
//            excludeColumnFiledNames.add("createTime");
//            excludeColumnFiledNames.add("updateBy");
//            excludeColumnFiledNames.add("updateTime");
//            excludeColumnFiledNames.add("remark");
//            excludeColumnFiledNames.add("isDelete");
//            excludeColumnFiledNames.add("params");
//        List<SensitiveWord> list = sensitivewordService.selectSensitiveWordList(sensitiveword);
//        ExcelUtil<SensitiveWord> util = new ExcelUtil<>(SensitiveWord. class);
//        util.exportExcel(response, list, "敏感词数据", excludeColumnFiledNames);
//    }
//
//
//    /**
//     * 获取 敏感词 详细信息
//     */
//    @Operation(summary = "根据ID获取详细信息")
//    @GetMapping("/{id}")
//    public R<SensitiveWord> getInfo(
//            @Parameter(name = "id", description = "ID", in = ParameterIn.PATH)
//            @PathVariable("id") Long id) {
//        return R.success(sensitivewordService.selectSensitiveWordById(id));
//    }
//
//
//    /**
//     * 新增 敏感词
//     */
//    @Operation(summary = "新增")
//    @PostMapping("/add")
//    public R<Integer> add(@RequestBody SensitiveWord sensitiveword) {
//        return R.success(sensitivewordService.insert(sensitiveword));
//    }
//
//
//    /**
//     * 修改 敏感词
//     */
//    @Operation(summary = "修改")
//    @PutMapping("/update")
//    public R<Integer> update(@RequestBody SensitiveWord sensitiveword) {
//        return R.success(sensitivewordService.update(sensitiveword));
//    }
//
//
//    /**
//     * 删除 敏感词
//     * /delete/1,2,3
//     */
//    @Operation(summary = "删除")
//    @DeleteMapping("/delete/{ids}")
//    public R<Integer> delete(@PathVariable("ids") Long[] ids) {
//        return R.success(sensitivewordService.delete(ids));
//    }
//
//
//}
