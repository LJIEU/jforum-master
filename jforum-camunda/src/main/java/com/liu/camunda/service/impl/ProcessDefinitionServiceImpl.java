package com.liu.camunda.service.impl;

import com.liu.camunda.service.ProcessDefinitionService;
import com.liu.camunda.vo.DefinitionVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:07
 */
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {
    public static final Logger log = LoggerFactory.getLogger(ProcessDefinitionServiceImpl.class);


    @Resource
    private RepositoryService repositoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> deploy(SysUser user, MultipartFile file, String bpmnName) {
        if (file.isEmpty()) {
            throw new ServiceException("无文件");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ServiceException("文件读取失败");
        }

        // 这里只是部署 但是还未启动
        Deployment deploy = repositoryService.createDeployment()
                .addInputStream("1.bpmn", inputStream)
                .name(bpmnName)
                // 使用 Model 实例
//                .addModelInstance("", )
                // 开启重复过滤
                .enableDuplicateFiltering(true)
                // 租户信息
//                .tenantId(user.getUserId().toString())
                .deploy();
        if (deploy == null) {
            throw new ServiceException("部署失败!");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessDefinition processDefinition = null;
//        log.info("部署ID:{}", deploy.getId());
        try {
            processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).list().get(0);
        } catch (Exception e) {
            // 删除部署
            this.deleteDeployment(deploy.getId());
            throw new ServiceException("部署出现异常");
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("deployId", deploy.getId());
        map.put("deployName", deploy.getName());
        map.put("processDefinitionId", processDefinition != null ? processDefinition.getId() : "获取流程定义失败~");
        return R.success(map);
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
        try {
            repositoryService.deleteDeployment(deploymentId);
        } catch (Exception ignored) {

        }
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
