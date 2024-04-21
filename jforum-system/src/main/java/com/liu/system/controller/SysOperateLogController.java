package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.db.entity.SysOperateLog;
import com.liu.db.service.SysOperateLogService;
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
 * 操作日志控制层 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/system/sysoperatelog")
public class SysOperateLogController extends BaseController {

    @Autowired
    private SysOperateLogService sysoperatelogService;

    /**
     * 查询 操作日志 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysoperatelog  操作日志对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
            @Parameter(name = "sysoperatelog" , description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysOperateLog>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "operate_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
        SysOperateLog sysoperatelog) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysOperateLog> list = sysoperatelogService.selectSysOperateLogList(sysoperatelog);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysOperateLog sysoperatelog) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
            excludeColumnFiledNames.add("updateBy");
            excludeColumnFiledNames.add("updateTime");
        List<SysOperateLog> list = sysoperatelogService.selectSysOperateLogList(sysoperatelog);
        ExcelUtil<SysOperateLog> util = new ExcelUtil<>(SysOperateLog. class);
        util.exportExcel(response, list, "操作日志数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 操作日志 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{operateId}")
    public R<SysOperateLog> getInfo(
            @Parameter(name = "operateId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("operateId") Long operateId) {
        return R.success(sysoperatelogService.selectSysOperateLogByOperateId(operateId));
    }


    /**
     * 新增 操作日志
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysOperateLog sysoperatelog) {
        return R.success(sysoperatelogService.insert(sysoperatelog));
    }


    /**
     * 修改 操作日志
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysOperateLog sysoperatelog) {
        return R.success(sysoperatelogService.update(sysoperatelog));
    }


    /**
     * 删除 操作日志
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{operateIds}")
    public R<Integer> delete(@PathVariable("operateIds") Long[] operateIds) {
        return R.success(sysoperatelogService.delete(operateIds));
    }


}
