package com.liu.camunda.controller.flow;

import com.liu.camunda.service.ProcessDefinitionService;
import com.liu.camunda.vo.DeployVo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Description: 流程部署
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:06
 */
@Tag(name = "流程部署功能")
@RestController
@RequestMapping("/my_camunda/process-definition")
public class ProcessDefinitionController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * 发布流程定义
     *
     * @param requestParam 请求参数
     * @return 提示信息
     */
    @Operation(summary = "流程部署")
    @PostMapping("/deploy")
    public R<Map<String, Object>> deployProcessDefinition(
            @RequestParam("bpmnName") String bpmnName,
            @RequestPart @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processDefinitionService.deploy(user, file, bpmnName);
    }

    /**
     * 发布流程定义
     *
     * @param deployVo 请求参数
     * @return 提示信息
     */
    @Operation(summary = "流程部署XML")
    @PostMapping("/deploy-xml")
    public R<Map<String, Object>> deployProcessDefinition(
            @RequestBody DeployVo deployVo, HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processDefinitionService.deployXml(user, deployVo);
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署id
     * @return 提示信息
     */
    @Operation(summary = "删除部署")
    @DeleteMapping("/deleteDeployment/{deploymentId}")
    public R<String> deleteDeployment(
            @Parameter(name = "deploymentId", description = "部署ID", in = ParameterIn.PATH)
            @PathVariable("deploymentId") String deploymentId) {
        // TODO 2024/4/30/16:05 不是什么人都可以删除部署
        return processDefinitionService.deleteDeployment(deploymentId);
    }

    /**
     * 挂起流程定义
     *
     * @param processDefinitionId 流程定义id
     * @return 提示信息
     */
    @Operation(summary = "暂停流程定义")
    @PostMapping("/suspendById/{id}")
    public R<String> suspendProcessDefinitionById(
            @Parameter(name = "id", description = "流程定义ID", in = ParameterIn.PATH)
            @PathVariable("id") String processDefinitionId) {
        return processDefinitionService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Operation(summary = "获取Bpmn模型实例")
    @GetMapping("/getBpmnModelInstance/{id}")
    public R<String> getBpmnModelInstance(
            @Parameter(name = "id", description = "流程定义ID", in = ParameterIn.PATH)
            @PathVariable("id") String processDefinitionId) {
        return processDefinitionService.getBpmnModelInstance(processDefinitionId);
    }

    @Operation(summary = "获取Bpmn流程")
    @GetMapping("/deployList")
    public R<List<DeployVo>> deployList() {
        return processDefinitionService.deployList();
    }


    @Operation(summary = "获取表单")
    @GetMapping("/formDataHtml/{deploymentId}")
    public R<String> formDataHtml(
            @Parameter(name = "deploymentId", description = "部署ID", in = ParameterIn.PATH)
            @PathVariable("deploymentId") String deploymentId) {
        return processDefinitionService.formDataHtml(deploymentId);
    }


//    /**
//     * json转为bpmn
//     *
//     * @param param 请求参数
//     */
//    @PostMapping("/convert")
//    public R<String> convert(@RequestBody @Validated DefinitionVo param) {
//        return processDefinitionService.convert(param);
//    }
}
