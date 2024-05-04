package com.liu.camunda.controller.flow;

import cn.hutool.core.util.StrUtil;
import com.liu.camunda.service.ProcessInstanceService;
import com.liu.camunda.service.ProcessTaskService;
import com.liu.camunda.vo.*;
import com.liu.core.controller.BaseController;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description: 流程定义模块
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:49
 */
@Tag(name = "流程实例模块")
@RestController
@RequestMapping("/my_camunda/process-instance")
public class ProcessInstanceController extends BaseController {


    private final ProcessInstanceService processInstanceService;
    private final ProcessTaskService processTaskService;

    public ProcessInstanceController(ProcessInstanceService processInstanceService, ProcessTaskService processTaskService) {
        this.processInstanceService = processInstanceService;
        this.processTaskService = processTaskService;
    }

    @Operation(summary = "查询当前用户代办任务 --> 单个业务")
    @GetMapping("/singleToDoTaskList")
    public R<List<TaskVo>> listToDoTaskBySingle(
            @Parameter(name = "businessKey", description = "业务Key", in = ParameterIn.QUERY)
            @RequestParam("businessKey") String businessKey,
            HttpServletRequest request) {
        /*
        这是由于 Camunda 中 一个流程实例ID是唯一的 但是 BusinessKey 可以不唯一 这个是一个标识
          一个 业务Key 对应 多个 任务
          多个 业务Key 对应 多个 任务
         */
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processTaskService.singleToDoTaskList(user, businessKey);
    }

    @Operation(summary = "查询当前用户代办任务 --> 多个业务")
    @GetMapping("/multiToDoTaskList")
    public R<List<TaskVo>> multiToDoTaskList(
            @Parameter(name = "businessKey", description = "业务Key", in = ParameterIn.QUERY)
            @RequestParam("businessKeys") String[] businessKeys,
            HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processTaskService.multiToDoTaskList(user, businessKeys);
    }

    @Operation(summary = "分页获取当前用户 待办任务")
    @GetMapping("/pageToDoTask")
    public R<List<TaskVo>> pageToDoTask(@RequestParam("businessKey") String businessKey,
                                        @RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize,
                                        HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processTaskService.pageToDoTask(user, businessKey, pageNum, pageSize);
    }

    @Operation(summary = "提交完成任务")
    @PostMapping("/completeSingleTask")
    public R<Map<String, String>> completeSingleTask(@RequestBody @Validated CompleteTaskVo requestParam) {
        // TODO 2024/4/22/14:19 这里还可以进行 留言 将任务提交后 再将 留言写入表中
        return processTaskService.completeSingleTask(requestParam);
    }

    @Operation(summary = "查询用户已办任务")
    @GetMapping("/listDoneTask")
    public R<List<TaskVo>> listDoneTask(
            @Parameter(name = "businessKey", description = "业务ID", in = ParameterIn.QUERY)
            @RequestParam("businessKey") String businessKey,
            HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processTaskService.listDoneTask(user, businessKey);
    }

    @Operation(summary = "驳回任务")
    @PostMapping("/rejectProcessInstance")
    public R<String> rejectProcessInstance(@RequestBody @Validated RejectInstanceVo requestParam) {
        return processInstanceService.rejectProcessInstance(requestParam);
    }

    @Operation(summary = "获取当前用户的所有审批记录[代办|已完成|发起|]")
    @PostMapping("/list")
    public R<List<ProcessVo>> list(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        return processInstanceService.list(pageNum, pageSize, user);
    }


    @Operation(summary = "根据流程实例ID获取所有流程")
    @PostMapping("/hist")
    public R<List<HistVo>> hist(
            @RequestParam(value = "processInstanceId") String processInstanceId,
            @RequestParam(value = "businessKey") String businessKey, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        return processInstanceService.hist(processInstanceId, businessKey, user);
    }

    @Operation(summary = "开启流程")
    @PostMapping("/start")
    public R<String> startByDeployId(
            @RequestBody Map<String, Object> params, HttpServletRequest request
    ) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("当前用户不存在!");
        }
        // 与前端写死  这个ID 是 部署ID
        String deployId = (String) params.get("id");
        if (StrUtil.isEmpty(deployId)) {
            return R.fail("部署ID为空!");
        }
        return processInstanceService.startProcessInstanceByDeployId(deployId, params, user);
    }


    @Operation(summary = "根据流程实例ID返回表单")
    @GetMapping("/formDataHtmlByInstanceId/{instanceId}")
    public R<Map<String, Object>> formHtmlByInstanceId(
            @Parameter(name = "instanceId", description = "流程实例ID", in = ParameterIn.PATH)
            @PathVariable("instanceId") String instanceId, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("当前用户不存在!");
        }
        return processInstanceService.formHtmlByInstanceId(instanceId, user);
    }

    @Operation(summary = "根据流程实例ID提交流程")
    @PostMapping("/complete")
    public R<String> startByInstanceId(
            @RequestBody Map<String, Object> params, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("当前用户不存在!");
        }
        String instanceId = (String) params.get("id");
        if (StrUtil.isEmpty(instanceId)) {
            return R.fail("流程ID为空!");
        }
        return processInstanceService.complete(instanceId, user, params);
    }
}
