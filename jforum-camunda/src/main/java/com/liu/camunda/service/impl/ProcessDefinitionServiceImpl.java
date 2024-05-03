package com.liu.camunda.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.liu.camunda.domin.BpmnInfo;
import com.liu.camunda.domin.FormField;
import com.liu.camunda.service.DeployAllNodeService;
import com.liu.camunda.service.ProcessDefinitionService;
import com.liu.camunda.utils.CamundaUtils;
import com.liu.camunda.vo.DefinitionVo;
import com.liu.camunda.vo.DeployVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.manager.AsyncManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    @Autowired
    private DeployAllNodeService deployAllNodeService;

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
                .addInputStream(bpmnSuffix(bpmnName), inputStream)
                .name(bpmnName.split("\\.")[0])
                // 使用 Model 实例
//                .addModelInstance("", )
                // 开启重复过滤
                .enableDuplicateFiltering(true)
                // 租户信息
//                .tenantId(user.getUserId().toString())
                .deploy();
        // 部署成功后 开启异步线程 去完成 该部署文件的节点读取任务 然后写入表中
        AsyncManager.manager().execute(new TimerTask() {
            @Override
            public void run() {
                byte[] bpmnResource = getBpmnResource(deploy.getId());
                deployAllNodeService.insert(deploy.getId(), bpmnResource);
            }
        });
        return R.success(deployInfo(deploy));
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
            deployAllNodeService.delete(deploymentId);
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

    @Override
    public R<Map<String, Object>> deployXml(SysUser user, DeployVo deployVo) {
        Deployment deploy = repositoryService.createDeployment()
                .addString(bpmnSuffix(deployVo.getBpmnId()), deployVo.getXml())
                .name(deployVo.getBpmnName()).enableDuplicateFiltering(true).deploy();
        return R.success(deployInfo(deploy));
    }

    @Override
    public R<List<DeployVo>> deployList() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        if (CollUtil.isEmpty(list)) {
            return R.success();
        }
        List<DeployVo> result = new ArrayList<>();
        for (Deployment deployment : list) {
            DeployVo deployVo = new DeployVo();
            deployVo.setBpmnId(deployment.getId());
            deployVo.setBpmnName(deployment.getName());
            // 获取源数据
            List<org.camunda.bpm.engine.repository.Resource> resources = repositoryService.getDeploymentResources(deployment.getId());
            for (org.camunda.bpm.engine.repository.Resource resource : resources) {
                String resourceName = resource.getName();
                InputStream inputStream = repositoryService.getResourceAsStream(deployment.getId(), resourceName);
                // 读取资源数据
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    // 获取资源数据
                    byte[] resourceData = outputStream.toByteArray();
                    deployVo.setXml(new String(resourceData));
                    // 关闭输入流
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            result.add(deployVo);
        }
        return R.success(result);
    }

    @Override
    public R<Map<String, Object>> formDataHtml(String deploymentId) {
        try {
            // 获取整理好的 BPMN 文件
            List<BpmnInfo> data = deployAllNodeService.selectByDeployId(deploymentId);
            // 返回第一个用户服务就行
            for (BpmnInfo info : data) {
                if (info.getStartEvent() != null) {
                    continue;
                }
                if (info.getUserTask() != null) {
                    // 获取第一个 用户任务就返回
                    List<FormField> formData = info.getFormData();
                    Map<String, Object> result = CamundaUtils.formDataResult(formData, data);
                    return R.success(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("解析失败");
        }
        return R.success();
    }

    private byte[] getBpmnResource(String deploymentId) {
        byte[] resourceData = null;
        List<org.camunda.bpm.engine.repository.Resource> resources = repositoryService.getDeploymentResources(deploymentId);
        for (org.camunda.bpm.engine.repository.Resource resource : resources) {
            String resourceName = resource.getName();
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            // 读取资源数据
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                // 获取资源数据
                resourceData = outputStream.toByteArray();
                // 关闭输入流
                inputStream.close();
                outputStream.close();
                return resourceData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return new String(resourceData);
        return resourceData;
    }

    private Map<String, Object> deployInfo(Deployment deploy) {
        if (deploy == null) {
            throw new ServiceException("部署失败!");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProcessDefinition processDefinition = null;
        try {
            processDefinition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deploy.getId()).list().get(0);
        } catch (Exception e) {
            // 删除部署
            this.deleteDeployment(deploy.getId());
            throw new ServiceException("部署出现异常");
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("deployId", deploy.getId());
        map.put("deployName", deploy.getName());
        map.put("processDefinitionId", processDefinition != null ? processDefinition.getId() : "获取流程定义失败~");
        return map;
    }

    /**
     * 处理后缀 .bpmn
     *
     * @param name 文件名
     * @return 返回 完整的xx.bpmn
     */
    private static String bpmnSuffix(String name) {
        String[] split = name.split("\\.");
        if ("bpmn".equals(split[split.length - 1])) {
            return name;
        }
        StringBuilder temp = new StringBuilder();
        for (String s : split) {
            temp.append(s);
        }
        temp.append(".bpmn");
        return temp.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(bpmnSuffix("1.bpmn")); // 1.bpmn
//        System.out.println(bpmnSuffix("1.")); // 1.bpmn
//        System.out.println(bpmnSuffix("xxx")); // xxx.bpmn
//    }

}
