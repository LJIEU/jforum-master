package com.liu.system.controller.test;

import cn.hutool.core.collection.CollUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.converter.TreeConverter;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.TreeUtils;
import com.liu.db.converter.TestTwoConverter;
import com.liu.db.entity.TestTwo;
import com.liu.db.service.TestTwoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 测试控制层 test_two
 *
 * @author JIE
 * @since 2024-04-02
 */
@Tag(name = "Tree测试")
@RestController
@RequestMapping("/system/testtwo")
public class TestTwoController extends BaseController {

    @Autowired
    private TestTwoService testtwoService;

    /**
     * 查询 测试 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param testtwo   测试对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
    })
    @GetMapping("/list")
    public R<List<TestTwo>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "two_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
            TestTwo testtwo) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<TestTwo> list = testtwoService.selectTestTwoList(testtwo);
        clearPage();
        return R.success(list);
    }

    /**
     * 树型结构 数据
     */
    @Operation(summary = "获取树型结构")
    @GetMapping("/tree")
    public R<List<TestTwo>> list(TestTwo testTwo) {
        List<TestTwo> list = testtwoService.selectTestTwoList(testTwo);
        // 整理成 树型结构
        TreeConverter<TestTwo> converter = new TestTwoConverter();
        List<TestTwo> treeList = TreeUtils.convertTree(list, converter);
        if (CollUtil.isEmpty(treeList)) {
            return R.success(list);
        } else {
            return R.success(treeList);
        }
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, TestTwo testtwo) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("remark");
        List<TestTwo> list = testtwoService.selectTestTwoList(testtwo);
        ExcelUtil<TestTwo> util = new ExcelUtil<>(TestTwo.class);
        util.exportExcel(response, list, "测试数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 测试 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{twoId}")
    public R<TestTwo> getInfo(
            @Parameter(name = "twoId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("twoId") Long twoId) {
        return R.success(testtwoService.selectTestTwoByTwoId(twoId));
    }


    /**
     * 新增 测试
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody TestTwo testtwo) {
        return R.success(testtwoService.insert(testtwo));
    }


    /**
     * 修改 测试
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody TestTwo testtwo) {
        return R.success(testtwoService.update(testtwo));
    }


    /**
     * 删除 测试
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{twoIds}")
    public R<Integer> delete(@PathVariable("twoIds") Long[] twoIds) {
        return R.success(testtwoService.delete(twoIds));
    }


}
