package com.liu.camunda.service.impl;

import com.liu.camunda.service.ExtendService;
import com.liu.camunda.vo.SequentialAddVo;
import com.liu.core.result.R;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 18:57
 */
@Service
public class ExtendServiceImpl implements ExtendService {
    /**
     * 历史服务  用于查询 正在进行和过去的流程实例信息
     * 与运行时服务 不同的是 在运行时信息仅包含如何给定时刻的实际运行时状态
     */
    @Resource
    private HistoryService historyService;
    /**
     * 运行时服务 提供对 Deployments ProcessDefinitions 和 ProcessInstances 的访问服务
     */
    @Resource
    private RuntimeService runtimeService;
    /**
     * 提供对 Task 任务 和 其他相关操作的服务
     */
    @Resource
    private TaskService taskService;
    /**
     * 提供对 流程定义 和 部署存储库 的访问服务
     */
    @Resource
    private RepositoryService repositoryService;

    @Override
    public R<String> addAssignee(SequentialAddVo requestParam) {
        String processInstanceId = requestParam.getProcessInstanceId();
        // 查询 历史流程实例列表 是否存在
        List<HistoricActivityInstance> hisActivityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).finished().list();
        // 获取 流程中活动的唯一标识符 [每进行一个流程就会创建一个活动标识UUID]
        List<String> activityIds = hisActivityList.stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());
        // 查询 运行流程实例 单一结果
        org.camunda.bpm.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 根据 流程实例 的流程定义ID号 获取 其 Bpmn模型元素
        BpmnModelInstance bpmnModel = repositoryService.getBpmnModelInstance(processInstance.getProcessDefinitionId());
        // 根据 节点ID 在 Bpmn模型中获取对应的 元素 用户任务
        ModelElementInstance modelElement = bpmnModel.getModelElementById(requestParam.getNodeId());
        UserTask userTask = (UserTask) modelElement;
        // 获取 参数值
        Collection<SequenceFlow> incoming = userTask.getIncoming();
        String transitionId = "";
        for (SequenceFlow sequenceFlow : incoming) {
            FlowNode source = sequenceFlow.getSource();
            if (activityIds.contains(source.getId())) {
                transitionId = sequenceFlow.getId();
                break;
            }
        }

        //获取当前环节实例
        ActivityInstance activity = runtimeService.getActivityInstance(processInstanceId);
        runtimeService.createProcessInstanceModification(processInstanceId)
                .cancelActivityInstance(activity.getId())
                .startTransition(transitionId)
                .execute();

        List<HistoricTaskInstance> hisTasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .taskDefinitionKey(requestParam.getNodeId()).taskDeleteReason("completed").list();
        List<String> assignees = hisTasks.stream().map(HistoricTaskInstance::getAssignee).collect(Collectors.toList());
        //之前已完成的任务此时自动审批
        automaticPass(processInstanceId, requestParam.getNodeId(), assignees);
        hisTasks.forEach(hisTask -> historyService.deleteHistoricTaskInstance(hisTask.getId()));
        return R.success();
    }

    private void automaticPass(String processInstanceId, String targetNodeId, List<String> assignees) {
        Task runTask = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(targetNodeId).singleResult();
        if (assignees.contains(runTask.getAssignee())) {
            taskService.complete(runTask.getId());
            automaticPass(processInstanceId, targetNodeId, assignees);
        }
    }
}
