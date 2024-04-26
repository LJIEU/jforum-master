package com.liu.camunda.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.liu.camunda.service.BpmPostService;
import com.liu.camunda.vo.BpmPostVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/26 15:43
 */
@Service
public class BpmPostServiceImpl implements BpmPostService {
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Override
    public R<Map<String, Object>> startPostFlow(BpmPostVo bpmPostVo, SysUser user) {
        // 获取流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(bpmPostVo.getProcessDefinitionId())
                .singleResult();
        if (processDefinition == null) {
            throw new ServiceException("未找到流程定义！");
        }
        // 启动流程实例
        String uuid = UUID.randomUUID().toString();
        ProcessInstantiationBuilder processInstantiationBuilder =
                runtimeService.createProcessInstanceByKey(processDefinition.getKey()).businessKey(uuid);

        // 业务Key 生成
        // 如果有需要传递的变量，可以在此设置  设置 发起者信息 ==》 当前用户名
        processInstantiationBuilder.setVariable("initiator", user.getUserName());
        processInstantiationBuilder.setVariable("postId", bpmPostVo.getPostId());

        ProcessInstance processInstance = processInstantiationBuilder.execute();

        // 检查流程实例是否成功启动
        if (processInstance == null) {
            throw new ServiceException("流程实例启动失败！");
        }

        Map<String, Object> result = MapUtil.newHashMap(3);
        // 流程定义ID
        result.put("processDefinitionId", processInstance.getProcessDefinitionId());
        // 流程实例ID
        result.put("processInstanceId", processInstance.getId());
        result.put("businessKey", processInstance.getBusinessKey());
        return R.success(result);
    }
}
