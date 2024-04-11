package com.liu.system.controller;

import cn.hutool.core.collection.CollUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.converter.LevelConverter;
import com.liu.core.converter.TreeConverter;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.LevelUtils;
import com.liu.core.utils.TreeUtils;
import com.liu.system.converter.SysDeptConverter;
import com.liu.system.converter.level.DeptLevelConverter;
import com.liu.system.dao.SysDept;
import com.liu.system.service.SysDeptService;
import com.liu.system.vo.level.Level;
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
 * 部门控制层 sys_dept
 *
 * @author JIE
 * @since 2024-04-11
 */
@Tag(name = "部门模块")
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysdeptService;

    /**
     * 查询 部门 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysdept   部门对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "sysdept", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysDept>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "dept_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            SysDept sysdept) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysDept> list = sysdeptService.selectSysDeptList(sysdept);
        clearPage();
        return R.success(list);
    }

    /**
     * 树型结构 数据
     */
    @Operation(summary = "获取树型结构")
    @GetMapping("/tree")
    public R<List<SysDept>> list(SysDept sysdept) {
        List<SysDept> list = sysdeptService.selectSysDeptList(sysdept);
        // 整理成 树型结构
        TreeConverter<SysDept> converter = new SysDeptConverter();
        List<SysDept> treeList = TreeUtils.convertTree(list, converter);
        if (CollUtil.isEmpty(treeList)) {
            return R.success(list);
        } else {
            return R.success(treeList);
        }
    }


    /**
     * 层次结构 数据
     */
    @Operation(summary = "获取树型结构")
    @GetMapping("/options")
    public R<Object> level() {
        List<SysDept> list = sysdeptService.selectSysDeptList(null);
        // 整理成 树型结构
        LevelConverter<SysDept, Level> converter = new DeptLevelConverter();
        List<Level> levelList = LevelUtils.buildTree(list, converter, 0L);
        if (CollUtil.isEmpty(levelList)) {
            return R.success(list);
        } else {
            return R.success(levelList);
        }
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysDept sysdept) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysDept> list = sysdeptService.selectSysDeptList(sysdept);
        ExcelUtil<SysDept> util = new ExcelUtil<>(SysDept.class);
        util.exportExcel(response, list, "部门数据", excludeColumnFiledNames);
    }


    /**
     * 获取 部门 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{deptId}")
    public R<SysDept> getInfo(
            @Parameter(name = "deptId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("deptId") Long deptId) {
        return R.success(sysdeptService.selectSysDeptByDeptId(deptId));
    }


    /**
     * 新增 部门
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysDept sysdept) {
        return R.success(sysdeptService.insert(sysdept));
    }


    /**
     * 修改 部门
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysDept sysdept) {
        return R.success(sysdeptService.update(sysdept));
    }


    /**
     * 删除 部门
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{deptIds}")
    public R<Integer> delete(@PathVariable("deptIds") Long[] deptIds) {
        return R.success(sysdeptService.delete(deptIds));
    }


}
