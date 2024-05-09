package com.liu.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.liu.core.constant.enume.BusinessType;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.DateUtils;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysOperateLog;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysOperateLogService;
import com.liu.db.service.SysUserService;
import com.liu.db.vo.OperateLogVo;
import com.liu.db.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 操作日志控制层 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/system/operate/log")
public class SysOperateLogController extends BaseController {

    @Autowired
    private SysOperateLogService sysoperatelogService;

    /**
     * 查询 操作日志 列表
     *
     * @param pageNum    当前页码
     * @param pageSize   页记录数
     * @param sortRules  排序规则
     * @param isDesc     是否逆序
     * @param operateLog 操作日志对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "moduleName", description = "所属模块"),
            @Parameter(name = "username", description = "用户名"),
            @Parameter(name = "nickName", description = "昵称"),
            @Parameter(name = "businessType", description = "操作类型"),
            @Parameter(name = "status", description = "状态"),
            @Parameter(name = "startTime", description = "开始时间"),
            @Parameter(name = "endTime", description = "结束时间"),
            @Parameter(name = "operateLog", description = "实体参数")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "operate_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            @RequestParam(value = "moduleName", required = false) String moduleName,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date startTime,
            @RequestParam(value = "endTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date endTime,
            SysOperateLog operateLog) {
        if (StrUtil.isNotEmpty(nickName)) {
            SysUser temp = new SysUser();
            temp.setNickName(nickName);
            List<SysUser> userList = SpringUtils.getBean(SysUserService.class).selectSysUserList(temp);
            if (CollUtil.isNotEmpty(userList)) {
                SysUser user = userList.get(0);
                username = user.getUserName();
            }
        }
        if (StrUtil.isNotEmpty(username)) {
            operateLog.setUsername(username);
        }
        if (StrUtil.isNotEmpty(moduleName)) {
            operateLog.setModuleName(moduleName);
        }
        if (StrUtil.isNotEmpty(businessType)) {
            // 注意一定要符合 操作平台的 那几种类型
            operateLog.setBusinessType(Integer.valueOf(businessType));
        }
        if (status != null) {
            operateLog.setStatus(status);
        }
        HashMap<String, Object> param = new HashMap<>(2);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        operateLog.setParams(param);
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysOperateLog> list = sysoperatelogService.selectSysOperateLogList(operateLog);
        PageInfo<SysOperateLog> pageInfo = new PageInfo<>(list);
        List<OperateLogVo> operateLogVos = list.stream().map(this::operateToVo).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", pageInfo.getTotal());
        result.put("data", operateLogVos);
        clearPage();
        return R.success(result);
    }

    @Operation(summary = "操作类型列表")
    @GetMapping("/businessTypeLevel")
    public R<List<Level>> businessTypeLevel() {
        List<Level> levelList = new ArrayList<>(BusinessType.values().length);
        for (BusinessType businessType : BusinessType.values()) {
            Level level = new Level();
            level.setValue(businessType.getValue());
            level.setLabel(businessType.getLabel());
            levelList.add(level);
        }
        return R.success(levelList);
    }

    @Operation(summary = "操作模块列表")
    @GetMapping("/moduleNameLevel")
    public R<List<Level>> moduleNameLevel() {
        List<String> moduleNames = sysoperatelogService.moduleNames();
        List<Level> levelList = new ArrayList<>(moduleNames.size());
        for (String moduleName : moduleNames) {
            Level level = new Level();
            level.setValue(moduleName);
            level.setLabel(moduleName);
            levelList.add(level);
        }
        return R.success(levelList);
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
        ExcelUtil<SysOperateLog> util = new ExcelUtil<>(SysOperateLog.class);
        util.exportExcel(response, list, "操作日志数据", excludeColumnFiledNames);
    }


    /**
     * 获取 操作日志 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{operateId}")
    public R<SysOperateLog> getInfo(
            @Parameter(name = "operateId", description = "ID", in = ParameterIn.PATH)
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


    public OperateLogVo operateToVo(SysOperateLog operateLog) {
        OperateLogVo vo = new OperateLogVo();
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(operateLog.getUsername());
        vo.setNickName(user.getNickName());
        vo.setModuleName(operateLog.getModuleName());
        vo.setIp(operateLog.getIp());
        vo.setBusinessType(operateLog.getBusinessType());
        vo.setDescribe(operateLog.getDescribe());
        // 时间转换
        String costTime = DateUtils.parseCostTime(operateLog.getCostTime());
        vo.setCostTime(costTime);
        vo.setStatus(operateLog.getStatus());
        vo.setOperateTime(operateLog.getCreateTime());
        return vo;
    }

}
