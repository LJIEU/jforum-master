package com.liu.camunda.flow;

import com.liu.camunda.service.ProcessDefinitionService;
import com.liu.camunda.vo.DefinitionVo;
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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 流程定义
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:06
 */
@Tag(name = "扩展功能")
@RestController
@RequiredArgsConstructor
@RequestMapping("/my_camunda/process-definition")
public class ProcessDefinitionController {

    private final ProcessDefinitionService processDefinitionService;

    /**
     * 发布流程定义
     *
     * @param requestParam 请求参数
     * @return 提示信息
     */
    @Operation(summary = "发布流程定义")
    @PostMapping("/deploy")
    public R<String> deployProcessDefinition(@RequestBody DeployVo requestParam, HttpServletRequest request) {
        String username = SecurityUtils.currentUsername(request);
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        return processDefinitionService.deploy(user, requestParam);
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署id
     * @return 提示信息
     */
    @Operation(summary = "删除部署")
    @DeleteMapping("/deleteDeployment")
    public R<String> deleteDeployment(
            @Parameter(name = "deploymentId", description = "部署ID", in = ParameterIn.QUERY)
            @RequestParam("deploymentId") String deploymentId) {
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

    /**
     * json转为bpmn
     *
     * @param param 请求参数
     */
    @PostMapping("/convert")
    public R<String> convert(@RequestBody @Validated DefinitionVo param) {
        return processDefinitionService.convert(param);
    }
}
