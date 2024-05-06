package com.liu.camunda.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.camunda.constants.BpmnConstants;
import com.liu.camunda.constants.CamundaState;
import com.liu.camunda.constants.StateEnum;
import com.liu.camunda.domin.BpmnInfo;
import com.liu.camunda.domin.FormField;
import com.liu.camunda.service.DeployAllNodeService;
import com.liu.camunda.service.ProcessInstanceService;
import com.liu.camunda.utils.CamundaUtils;
import com.liu.camunda.vo.HistVo;
import com.liu.camunda.vo.ProcessVo;
import com.liu.camunda.vo.RejectInstanceVo;
import com.liu.camunda.vo.StartProcessVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:53
 */
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
    private static final Logger log = LoggerFactory.getLogger(ProcessInstanceServiceImpl.class);

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;


    @Resource
    private IdentityService identityService;


    @Override
    public R<String> startProcessInstanceByKey(SysUser user, StartProcessVo requestParam) {
        String processDefinitionKey = requestParam.getProcessDefinitionKey();
        HashMap<String, Object> paramMap = new HashMap<>(2);
        List<String> assigneeList = new ArrayList<>();
        assigneeList.add("");
        paramMap.put("initiator", "1");
        paramMap.put("assigneeList", assigneeList);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
                requestParam.getBusinessKey(), paramMap);
        if (processInstance == null) {
            throw new ServiceException("流程定义启动失败~");
        }
        return R.success(processInstance.getProcessInstanceId());
    }

    @Override
    public R<String> startProcessInstanceById(SysUser user, StartProcessVo requestParam) {
        String processDefinitionKey = requestParam.getProcessDefinitionId();
        // 流程信息参数[发起者|谁执行|...]
        HashMap<String, Object> paramMap = new HashMap<>(5);
        paramMap.put("initiator", user.getUserId().toString());
        // TODO 2024/4/22/13:34 操作人员  如果是帖子那就是发给帖子审核员 获取到帖子审核员的ID
        List<String> assigneeList = new ArrayList<String>();
        paramMap.put("xxx", "xxx");

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionKey,
                requestParam.getBusinessKey(), paramMap);
        if (processInstance == null) {
            throw new ServiceException("流程定义启动失败~");
        }
        return R.success(processInstance.getProcessInstanceId());
    }

    @Override
    public R<String> rejectProcessInstance(RejectInstanceVo requestParam) {
        String processInstanceId = requestParam.getProcessInstanceId();
        ActivityInstance activity = runtimeService.getActivityInstance(processInstanceId);
        runtimeService.createProcessInstanceModification(processInstanceId)
                .cancelActivityInstance(activity.getId())
                // 注释
                .setAnnotation("驳回")
                // 返回之前节点
                .startBeforeActivity(requestParam.getTargetNodeId())
                .execute();
        return R.success("驳回成功~");
    }

    @Override
    public R<List<ProcessVo>> list(Integer pageNum, Integer pageSize, SysUser user) {
        List<ProcessVo> inProgressList = new ArrayList<>();
        List<ProcessVo> finishList = new ArrayList<>();
        List<ProcessVo> myDoToList = new ArrayList<>();
        // 1. 我发起的流程
        populateInProgressAndFinishLists(user, inProgressList, finishList);
        // 2. 我待办的流程
        populateMyDoToList(user, myDoToList);
        // 对完成的流程进行特殊处理
        isReject(finishList, StateEnum.REJECT);
        // 整合结果列表并排序
        List<ProcessVo> result = mergeAndSortProcessVos(inProgressList, finishList, myDoToList, user);
        return R.success(result);
    }

    /**
     * 整理 由我发起的任务流程
     *
     * @param user           用户
     * @param inProgressList 进行中
     * @param finishList     完成的
     */
    private void populateInProgressAndFinishLists(SysUser user, List<ProcessVo> inProgressList, List<ProcessVo> finishList) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .startedBy(user.getUserId().toString()).list();

        for (HistoricProcessInstance instance : list) {
            ProcessVo processVo = toProcessVo(instance.getProcessDefinitionName(), instance.getId(), instance.getBusinessKey(), StateEnum.OTHER, instance.getStartTime(), true);
            switch (instance.getState()) {
                case CamundaState.ACTIVE:
                    processVo.setState(StateEnum.REVIEW.getState());
                    processVo.setTagType(StateEnum.REVIEW.getTagType());
                    inProgressList.add(processVo);
                    break;
                case CamundaState.INTERNALLY_TERMINATED:
                    processVo.setState(StateEnum.DELETE.getState());
                    processVo.setTagType(StateEnum.DELETE.getTagType());
                    break;
                case CamundaState.COMPLETED:
                    processVo.setState(StateEnum.COMPLETE.getState());
                    processVo.setTagType(StateEnum.COMPLETE.getTagType());
                    finishList.add(processVo);
                    break;
                default:
                    // 处理其他状态
                    break;
            }
        }
    }

    /**
     * 整理我的待办任务流程
     *
     * @param user       用户
     * @param myDoToList 待办任务列表
     */
    private void populateMyDoToList(SysUser user, List<ProcessVo> myDoToList) {
        List<Task> taskList = taskService.createTaskQuery().active().list();
        for (Task task : taskList) {
            if (StrUtil.isEmpty(task.getAssignee())) {
                try {
                    List<String> userIds = (List<String>) taskService.getVariable(task.getId(), BpmnConstants.CANDIDATE_USERS);
                    if (userIds != null && userIds.contains(user.getUserId().toString())) {
                        myDoToList.add(taskToProcessVo(task));
                    }
                } catch (Exception e) {
                    // 记录异常，而不是忽略
                    log.error("获取任务变量时出错: ", e);
                }
            } else if (isReject(task)) {
                ProcessVo processVo = taskToProcessVo(task);
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId()).singleResult();
                if (historicProcessInstance != null) {
                    String startUserId = historicProcessInstance.getStartUserId();
                    if (user.getUserId().toString().equals(startUserId)) {
                        processVo.setState(StateEnum.MY_REJECT.getState());
                        processVo.setTagType(StateEnum.MY_REJECT.getTagType());
                    } else {
                        processVo.setState(StateEnum.REJECT.getState());
                        processVo.setTagType(StateEnum.REJECT.getTagType());
                    }
                    myDoToList.add(processVo);
                }
            }
        }
    }

    /**
     * 对这些列表进行排序
     *
     * @param inProgressList 进行中
     * @param finishList     完成的
     * @param myDoToList     待办
     * @param user           用户
     * @return 返回排序结果
     */
    private List<ProcessVo> mergeAndSortProcessVos(List<ProcessVo> inProgressList, List<ProcessVo> finishList, List<ProcessVo> myDoToList, SysUser user) {
        List<ProcessVo> result = new ArrayList<>();
        result.addAll(inProgressList);
        result.addAll(finishList);
        myDoToList.sort((o1, o2) -> {
            String state1 = o1.getState();
            String state2 = o2.getState();
            if (o1.getUserId().equals(user.getUserId().toString())) {
                return -1;
            } else if (o2.getUserId().equals(user.getUserId().toString())) {
                return 1;
            }
            return state1.equals(state2) ? 0 : o1.getStartTime().getTime() < o2.getStartTime().getTime() ? 1 : -1;
        });
        result.addAll(myDoToList);
        return result;
    }


    /**
     * 判断是否是 驳回
     *
     * @param task 任务
     */
    private boolean isReject(Task task) {
        AtomicBoolean flag = new AtomicBoolean(false);
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
        for (HistoricActivityInstance v : activityInstances) {
            if (v.getTaskId() != null && v.getTaskId().equals(task.getId())) {
                // 找到节点
                String activityId = v.getActivityId();
                // 继续查看是否存在 相同节点 如果存在 那就存在驳回
                int count = 0;
                for (HistoricActivityInstance activityInstance : activityInstances) {
                    if (activityInstance.getTaskId() != null && activityId.equals(activityInstance.getActivityId())) {
                        count++;
                    }
                }
                if (count >= 2) {
                    flag.set(true);
                }
            }
        }
        return flag.get();
    }

    /**
     * 是否 驳回 流程
     *
     * @param processVoList 列表
     * @param stateEnum     状态
     */
    private void isReject(List<ProcessVo> processVoList, StateEnum stateEnum) {
        if (CollUtil.isEmpty(processVoList)) {
            return;
        }
        for (ProcessVo processVo : processVoList) {
            String value = null;
            try {
                value = (String) historyService.createHistoricVariableInstanceQuery().processInstanceId(processVo.getProcessInstanceId()).variableName(BpmnConstants.OPTIONS).singleResult().getValue();
            } catch (Exception e) {
                // 说明不存在驳回数据 直接 进行下一层
                continue;
            }
            if ("1".equals(value)) {
                // 驳回
                processVo.setState(stateEnum.getState());
                processVo.setTagType(stateEnum.getTagType());
            }
        }
    }

    @Override
    public R<List<HistVo>> hist(String processInstanceId, String businessKey, SysUser user) {
        List<HistVo> result = new ArrayList<>();
        List<HistoricTaskInstance> histTask = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).list();
        for (HistoricTaskInstance v : histTask) {
            // 获取 BPMN 对应的节点ID
            String id = v.getTaskDefinitionKey();
            HistVo histVo = new HistVo();
            histVo.setTackId(v.getId());
            histVo.setName(v.getName());
            // 设置 最后执行者信息
            if (StrUtil.isNotEmpty(v.getAssignee())) {
                SysUser nodeUser = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(Long.valueOf(v.getAssignee()));
                histVo.setAvatar(nodeUser.getAvatar());
                histVo.setUsername(nodeUser.getUserName());
                histVo.setNickName(nodeUser.getNickName());
            }
            // 0 完成 1进行中 duration
            histVo.setState(ObjUtil.isEmpty(v.getEndTime()) ? 1 : 0);
            // 执行者
            histVo.setLastUser(v.getAssignee());
            histVo.setStartTime(v.getStartTime());
            histVo.setEndTime(v.getEndTime());
            result.add(histVo);
        }
        result = result.stream().sorted((o1, o2) -> Math.toIntExact(o1.getStartTime().getTime() - o2.getStartTime().getTime())).collect(Collectors.toList());
        // 排序之后进行 整理是否是驳回的
        if (CollUtil.isNotEmpty(result)) {
            HistVo histVo = result.get(0);
            if (StrUtil.isNotEmpty(histVo.getLastUser())) {
                SysUser nodeUser = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(Long.valueOf(histVo.getLastUser()));
                histVo.setAvatar(nodeUser.getAvatar());
                histVo.setUsername(nodeUser.getUserName());
                histVo.setNickName(nodeUser.getNickName());
            }
        }
        // 排序好的列表
        for (int i = 0; i < result.size() - 1; i++) {
            String name = result.get(i).getName();
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(j).getName().equals(name)) {
                    // 那说明是被驳回的 那其前一个节点就是审批者 设置状态为驳回
                    result.get(j - 1).setState(2);
                    try {
                        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId)
                                .variableName(BpmnConstants.OPINION).singleResult();
                        String opinion = (String) historicVariableInstance.getValue();
                        if (StrUtil.isNotEmpty(opinion)) {
                            result.get(j - 1).setOpinion(opinion);
                        }
                    } catch (Exception ignored) {
                    }
                    // i移动位置到当前
                    i = j - 1;
                }
            }
        }
        return R.success(result);
    }

    @Override
    public R<String> startProcessInstanceByDeployId(String deployId, Map<String, Object> params, SysUser user) {
        List<BpmnInfo> bpmnInfoList = SpringUtils.getBean(DeployAllNodeService.class).selectByDeployId(deployId);
        List<String> fieldList = new ArrayList<>();
        for (BpmnInfo bpmnInfo : bpmnInfoList) {
            if (bpmnInfo.getUserTask() != null) {
                for (FormField field : bpmnInfo.getFormData()) {
                    fieldList.add(field.getId());
                }
                break;
            }
        }
        // 获取流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        if (processDefinition == null) {
            throw new ServiceException("未找到流程定义！");
        }
        // 创建流程实例
        ProcessInstantiationBuilder processInstantiationBuilder =
                runtimeService.createProcessInstanceByKey(processDefinition.getKey())
                        .businessKey(BpmnConstants.POST_BUSINESS);

        // 设置 流程发起者ID 后期使用 getStartUserId()
        identityService.setAuthenticatedUserId(user.getUserId().toString());

        // 如果有需要传递的变量，可以在此设置  设置 发起者信息 ==》 当前用户ID
        processInstantiationBuilder.setVariable(BpmnConstants.INITIATOR, user.getUserId().toString());
        for (String field : fieldList) {
            processInstantiationBuilder.setVariable(field, params.get(field));
        }
        // 启动流程实例
        ProcessInstance processInstance = processInstantiationBuilder.execute();
        // 检查流程实例是否成功启动
        if (processInstance == null) {
            throw new ServiceException("流程实例启动失败！");
        }

        // 发起
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        taskService.setAssignee(task.getId(), user.getUserId().toString());
        taskService.complete(task.getId());
        return R.success();
    }


    @Override
    public R<Map<String, Object>> formHtmlByInstanceId(String instanceId, SysUser user) {
        // 查询部署ID
        BpmnInfo node = currNode(instanceId);
        if (node == null) {
            return R.success();
        }
        // 这个就是当前节点
        List<FormField> formData = node.getFormData();
        // 整理数据表单
        Map<String, Object> result = CamundaUtils.formDataResult(formData, allNode(instanceId));
        return R.success(result);
    }

    /**
     * 返回当前节点 或者 全部
     *
     * @param instanceId 流程实例ID
     * @return 返回当前节点信息
     */
    private BpmnInfo currNode(String instanceId) {
        // 查询已完成的节点 没有结束时间的 就是当前节点任务
        String activityId = "";
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).list();
        for (HistoricActivityInstance instance : list) {
            if (instance.getEndTime() == null) {
                activityId = instance.getActivityId();
                break;
            }
        }
        if (StrUtil.isEmpty(activityId)) {
            return null;
        }
        List<BpmnInfo> bpmnInfoList = allNode(instanceId);
        BpmnInfo node = null;
        int index = 0;
        for (BpmnInfo bpmnInfo : bpmnInfoList) {
            if (bpmnInfo.getId().equals(activityId)) {
                node = bpmnInfoList.get(index);
                return node;
            }
            index++;
        }
        return null;
    }

    /**
     * 获取所有节点
     *
     * @param instanceId 实例ID
     * @return 返回已经整理好顺序的节点
     */
    private List<BpmnInfo> allNode(String instanceId) {
        // 流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        // 流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        return SpringUtils.getBean(DeployAllNodeService.class).selectByDeployId(processDefinition.getDeploymentId());
    }

    @Override
    public R<String> complete(String instanceId, SysUser user, Map<String, Object> params) {
        BpmnInfo bpmnInfo = currNode(instanceId);
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
/*
        if (MapUtil.isNotEmpty(params)) {
            String options = (String) params.get(BpmnConstants.OPTIONS);
            String option = (String) params.get(BpmnConstants.OPINION);
            if ("1".equals(options)) {
                // 驳回
                String[] forActivity = getInstanceIdForActivity(instanceId);
                taskService.createComment(task.getId(), instanceId, option);
//                ActivityInstance activity = runtimeService.getActivityInstance(instanceId);
                runtimeService.createProcessInstanceModification(instanceId)
                        .setAnnotation("驳回任务~")
                        // 取消当前任务
                        .cancelActivityInstance(forActivity[0])
//                        .cancelActivityInstance(activity.getId())
                        // 回退上级任务
                        .startBeforeActivity(forActivity[1])
                        .execute();
                return R.success("驳回成功");
            }
        }
*/
        // 如果有需要传递的变量，可以在此设置  设置 发起者信息 ==》 当前用户ID
        taskService.setVariable(task.getId(), BpmnConstants.INITIATOR, user.getUserId().toString());
        List<String> fieldList = new ArrayList<>();
        if (bpmnInfo != null && bpmnInfo.getFormData() != null) {
            for (FormField field : bpmnInfo.getFormData()) {
                fieldList.add(field.getId());
            }
            for (String field : fieldList) {
                taskService.setVariable(task.getId(), field, params.get(field));
            }
        }

        // 按开始时间升序排序
        // 删除上一个任务传递过来的变量信息
        List<HistoricTaskInstance> histTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).list().stream().sorted((o1, o2) -> Math.toIntExact(o1.getStartTime().getTime() - o2.getStartTime().getTime())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(histTaskList) && histTaskList.size() > 1) {
            for (int i = 1; i < histTaskList.size(); i++) {
                if (histTaskList.get(i).getId().equals(task.getId())) {
                    // 找到了 --> 获取其上一个任务节点
                    String pre = histTaskList.get(i - 1).getId();
                    // 去查找其变量信息 删除变量
                    try {
                        Map<String, Object> variables = taskService.getVariables(pre);
                        if (variables != null && variables.size() > 0) {
                            for (String key : variables.keySet()) {
                                // 意见不进行删除
                                if (!key.equals(BpmnConstants.OPINION)) {
                                    taskService.removeVariable(pre, key);
                                }
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        }

//        taskService.removeVariable(task.getId(), BpmnConstants.CANDIDATE_USERS);

        // 我完成的
        taskService.setAssignee(task.getId(), user.getUserId().toString());
        taskService.complete(task.getId());

        return R.success();
    }

    /**
     * 当前活动的实例ID
     *
     * @param instanceId 流程实例
     * @return 返回ID[停止, 启动]  ==> 获取的是执行ID Execution_ID
     */
    private String[] getInstanceIdForActivity(String instanceId) {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(instanceId).list().stream().sorted((o1, o2) -> {
                    return Math.toIntExact(o2.getStartTime().getTime() - o1.getStartTime().getTime());
                }).collect(Collectors.toList());
        String cancelId = historicTaskInstanceList.get(historicTaskInstanceList.size() - 1).getId();
        String startId = historicTaskInstanceList.get(historicTaskInstanceList.size() - 2).getId();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).list();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            if (activityInstance.getTaskId() != null) {
                if (activityInstance.getTaskId().equals(cancelId)) {
                    cancelId = activityInstance.getExecutionId();
                } else if (activityInstance.getTaskId().equals(startId)) {
                    startId = activityInstance.getExecutionId();
                }
            }
        }
        return new String[]{cancelId, startId};
    }

    @Override
    public R<String> removeProcessInstance(String processInstanceId, SysUser user) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, "删除~");
        } catch (Exception ignored) {
        }
        return R.success();
    }

    private ProcessVo taskToProcessVo(Task task) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        return toProcessVo(repositoryService.getProcessDefinition(task.getProcessDefinitionId()).getName(),
                task.getProcessInstanceId(), historicProcessInstance.getBusinessKey(), StateEnum.UPCOMING, task.getCreateTime(), false);
    }

    private ProcessVo toProcessVo(String name, String processInstanceId, String businessKey, StateEnum stateEnum, Date date, Boolean myInitiate) {
        ProcessVo processVo = new ProcessVo();
        processVo.setId(IdUtil.fastSimpleUUID());
        processVo.setTagType(stateEnum.getTagType());
        processVo.setStartTime(date);
        processVo.setProcessInstanceId(processInstanceId);
        processVo.setState(stateEnum.getState());
        processVo.setName(name);
        processVo.setMyInitiate(myInitiate);
        processVo.setBusinessKey(businessKey);
        // 设置 发起者用户信息
        String userId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getStartUserId();
        SysUser user = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(Long.valueOf(userId));
        processVo.setUserId(userId);
        processVo.setUsername(user.getUserName());
        processVo.setNickName(user.getNickName());
        processVo.setAvatar(user.getAvatar());
        return processVo;
    }
}
