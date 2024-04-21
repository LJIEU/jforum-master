package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.db.entity.SysConfig;
import com.liu.db.service.SysConfigService;
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
 * 参数配置控制层 sys_config
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "系统设置")
@RestController
@RequestMapping("/system/sysconfig")
public class SysConfigController extends BaseController {

    @Autowired
    private SysConfigService sysconfigService;

    /**
     * 查询 参数配置 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysconfig  参数配置对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
            @Parameter(name = "sysconfig" , description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysConfig>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "config_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
        SysConfig sysconfig) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysConfig> list = sysconfigService.selectSysConfigList(sysconfig);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysConfig sysconfig) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysConfig> list = sysconfigService.selectSysConfigList(sysconfig);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig. class);
        util.exportExcel(response, list, "参数配置数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 参数配置 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{configId}")
    public R<SysConfig> getInfo(
            @Parameter(name = "configId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("configId") Integer configId) {
        return R.success(sysconfigService.selectSysConfigByConfigId(configId));
    }


    /**
     * 新增 参数配置
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysConfig sysconfig) {
        return R.success(sysconfigService.insert(sysconfig));
    }


    /**
     * 修改 参数配置
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysConfig sysconfig) {
        return R.success(sysconfigService.update(sysconfig));
    }


    /**
     * 删除 参数配置
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{configIds}")
    public R<Integer> delete(@PathVariable("configIds") Integer[] configIds) {
        return R.success(sysconfigService.delete(configIds));
    }


}
