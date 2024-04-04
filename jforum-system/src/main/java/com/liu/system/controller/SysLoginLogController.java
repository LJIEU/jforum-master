package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.system.dao.SysLoginLog;
import com.liu.system.service.SysLoginLogService;
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
 * 系统访问记录控制层 sys_login_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "用户登录日志")
@RestController
@RequestMapping("/system/sysloginlog")
public class SysLoginLogController extends BaseController {

    @Autowired
    private SysLoginLogService sysloginlogService;

    /**
     * 查询 系统访问记录 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysloginlog  系统访问记录对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum" , description = "当前页" , example = "1"),
            @Parameter(name = "pageSize" , description = "页大小" , example = "10"),
            @Parameter(name = "sortRules" , description = "排序规则"),
            @Parameter(name = "isDesc" , description = "是否逆序排序"),
            @Parameter(name = "sysloginlog" , description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<SysLoginLog>> list(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules" , defaultValue = "login_id") String sortRules,
            @RequestParam(value = "isDesc" , defaultValue = "false") Boolean isDesc,
        SysLoginLog sysloginlog) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysLoginLog> list = sysloginlogService.selectSysLoginLogList(sysloginlog);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysLoginLog sysloginlog) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
            excludeColumnFiledNames.add("createBy");
            excludeColumnFiledNames.add("updateBy");
            excludeColumnFiledNames.add("updateTime");
            excludeColumnFiledNames.add("remark");
            excludeColumnFiledNames.add("isDelete");
        List<SysLoginLog> list = sysloginlogService.selectSysLoginLogList(sysloginlog);
        ExcelUtil<SysLoginLog> util = new ExcelUtil<>(SysLoginLog. class);
        util.exportExcel(response, list, "系统访问记录数据" , excludeColumnFiledNames);
    }


    /**
     * 获取 系统访问记录 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{loginId}")
    public R<SysLoginLog> getInfo(
            @Parameter(name = "loginId" , description = "ID" , in = ParameterIn.PATH)
            @PathVariable("loginId") Long loginId) {
        return R.success(sysloginlogService.selectSysLoginLogByLoginId(loginId));
    }


    /**
     * 新增 系统访问记录
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody SysLoginLog sysloginlog) {
        return R.success(sysloginlogService.insert(sysloginlog));
    }


    /**
     * 修改 系统访问记录
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody SysLoginLog sysloginlog) {
        return R.success(sysloginlogService.update(sysloginlog));
    }


    /**
     * 删除 系统访问记录
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{loginIds}")
    public R<Integer> delete(@PathVariable("loginIds") Long[] loginIds) {
        return R.success(sysloginlogService.delete(loginIds));
    }


}
