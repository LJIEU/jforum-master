package com.liu.system.controller.test;


import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.system.dao.TestOne;
import com.liu.system.service.TestOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 测试控制层 test_one
 *
 * @author JIE
 * @since 2024-03-31
 */
@Tag(name = "TestOne")
@RestController
@RequestMapping("/system/testone")
public class TestOneController extends BaseController {

    @Autowired
    private TestOneService testoneService;

    /**
     * 查询 测试 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param testone   测试对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "testone", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<TestOne>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "one_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            TestOne testone) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<TestOne> list = testoneService.selectTestOneList(testone);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, TestOne testone) {
        List<TestOne> list = testoneService.selectTestOneList(testone);
        ExcelUtil<TestOne> util = new ExcelUtil<>(TestOne.class);
        // 根据用户传入字段 假设我们要忽略 isDelete
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("isDelete");
        util.exportExcel(response, list, "测试数据", excludeColumnFiledNames);
    }


    /**
     * 获取 测试 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{oneId}")
    public R<TestOne> getInfo(
            @Parameter(name = "oneId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("oneId") Long oneId) {
        return R.success(testoneService.selectTestOneByOneId(oneId));
    }


    /**
     * 新增 测试
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@Valid @RequestBody TestOne testone) {
        return R.success(testoneService.insert(testone));
    }


    /**
     * 修改 测试
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@Valid @RequestBody TestOne testone) {
        return R.success(testoneService.update(testone));
    }


    /**
     * 删除 测试
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{oneIds}")
    public R<Integer> delete(@PathVariable("oneIds") Long[] oneIds) {
        return R.success(testoneService.delete(oneIds));
    }


}
