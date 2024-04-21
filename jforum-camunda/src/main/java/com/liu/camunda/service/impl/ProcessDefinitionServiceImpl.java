package com.liu.camunda.service.impl;

import com.liu.camunda.service.ProcessDefinitionService;
import com.liu.camunda.vo.DefinitionVo;
import com.liu.camunda.vo.DeployVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:07
 */
@Slf4j
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Resource
    private RepositoryService repositoryService;

    @Override
    public R<String> deploy(SysUser user, DeployVo param) {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource(param.getResourcePath())
                .name(param.getBpmnName()).tenantId(user.getUserId().toString()).deploy();
        if (deploy == null) {
            throw new ServiceException("部署失败!");
        }
        return R.success(deploy.getId());
    }

    @Override
    public R<String> getBpmnModelInstance(String processDefinitionId) {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);
        if (bpmnModelInstance != null) {
            Collection<UserTask> userTasks = bpmnModelInstance.getModelElementsByType(UserTask.class);
            Definitions definitions = bpmnModelInstance.getDefinitions();
            log.info("Bpmn模型信息:{}\n{}", userTasks, definitions);
        }
        return null;
    }

    @SuppressWarnings("AlibabaRemoveCommentedCode")
    @Override
    public R<String> deleteDeployment(String deploymentId) {
        // 级联删除  删除其绑定的流程实例和job
        repositoryService.deleteDeployment(deploymentId);
        // true 则是不进行 级联删除
        // repositoryService.deleteDeployment(deploymentId, true);
        return R.success("删除成功~");
    }

    @Override
    public R<String> suspendProcessDefinitionById(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
        return R.success("流程暂停~");
    }

    @Override
    public R<String> convert(DefinitionVo param) {
        return null;
    }
}
