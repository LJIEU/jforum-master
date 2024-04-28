package com.liu.camunda.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.camunda.constants.BpmConstants;
import com.liu.camunda.constants.CamundaState;
import com.liu.camunda.constants.StateEnum;
import com.liu.camunda.service.ProcessInstanceService;
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
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
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
        // 获取所有已完成的
        List<ProcessVo> result = new ArrayList<>();
        List<ProcessVo> myInitiatives = new ArrayList<>();
        // 1. 我发起的流程
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .startedBy(user.getUserId().toString()).list();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                ProcessVo processVo = toProcessVo(v.getProcessDefinitionName(), v.getId(), v.getBusinessKey(), StateEnum.OTHER, v.getStartTime(), true);
                // 状态
                if (CamundaState.ACTIVE.equals(v.getState())) {
                    processVo.setState(StateEnum.REVIEW.getState());
                    processVo.setTagType(StateEnum.REVIEW.getTagType());
                } else if (CamundaState.INTERNALLY_TERMINATED.equals(v.getState())) {
                    processVo.setState(StateEnum.DELETE.getState());
                    processVo.setTagType(StateEnum.DELETE.getTagType());
                } else if (CamundaState.COMPLETED.equals(v.getState())) {
                    processVo.setState(StateEnum.COMPLETE.getState());
                    processVo.setTagType(StateEnum.COMPLETE.getTagType());
                }
                myInitiatives.add(processVo);
            });
        }
        // 2. 我代办的流程[去正在运行的任务中寻找]
        List<ProcessVo> myDoToList = new ArrayList<>();
        List<Task> taskList = taskService.createTaskQuery().active().list();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> userIds = null;
            for (Task task : taskList) {
                if (StrUtil.isEmpty(task.getAssignee())) {
                    // 去查询变量
                    try {
                        userIds = (List<String>) taskService.getVariable(task.getId(), BpmConstants.CANDIDATE_USERS);
                        if (Objects.requireNonNull(userIds).contains(user.getUserId().toString())) {
                            myDoToList.add(taskToProcessVo(task));
                        }
                    } catch (Exception ignored) {

                    }
                } else if (task.getAssignee().equals(user.getUserId().toString())) {
                    myDoToList.add(taskToProcessVo(task));
                }
            }
        }
        // 3. 我完成的流程
        List<ProcessVo> myCompleted = new ArrayList<>();
        List<HistoricTaskInstance> completedTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(user.getUserId().toString()).finished()
                .taskDeleteReason(CamundaState.COMPLETED.toLowerCase(Locale.ROOT)).list();
        for (HistoricTaskInstance completedTask : completedTasks) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(completedTask.getProcessInstanceId()).singleResult();
            ProcessVo processVo = toProcessVo(repositoryService.getProcessDefinition(completedTask.getProcessDefinitionId()).getName(),
                    completedTask.getProcessInstanceId(), historicProcessInstance.getBusinessKey(), StateEnum.COMPLETE, completedTask.getStartTime(),
                    false);
            myCompleted.add(processVo);
        }
        // 整理数据
        // 我发起的  我代办的  我完成的
        isReject(myCompleted);
        isReject(myInitiatives);
        // 合并
        result.addAll(myInitiatives);
        result.addAll(myDoToList);
        // 我完成的进行筛选 状态 和 流程实例ID一样则 时间为第一个流程实例的开始时间  第二个流程实例的结束时间
//        List<ProcessVo> newMyCompleted = new ArrayList<ProcessVo>();
        for (int i = 0; i < myCompleted.size(); i++) {
            ProcessVo processVo = myCompleted.get(i);
            for (int j = 0; j < myCompleted.size(); j++) {
                if (i != j && processVo.getProcessInstanceId().equals(myCompleted.get(j).getProcessInstanceId())) {
                    Date startTime = processVo.getStartTime();
                    // 遇到一样的实例
                    if (processVo.getStartTime().getTime() > myCompleted.get(j).getStartTime().getTime()) {
                        startTime = myCompleted.get(j).getStartTime();
                    }
                    // 删除 驳回的流程 然后 审批通过
                    myCompleted.get(j).setStartTime(startTime);
                    myCompleted.removeIf(v -> v.equals(processVo));
                    i--;
                }
            }
        }
        result.addAll(myCompleted);
        return R.success(result);
    }

    /**
     * 是否 驳回 流程
     *
     * @param processVoList 列表
     */
    private void isReject(List<ProcessVo> processVoList) {
        for (ProcessVo processVo : processVoList) {
            String value = (String) historyService.createHistoricVariableInstanceQuery().processInstanceId(processVo.getProcessInstanceId()).variableName(BpmConstants.OPTIONS).singleResult().getValue();
            if ("1".equals(value)) {
                // 驳回
                processVo.setState(StateEnum.REJECT.getState());
                processVo.setTagType(StateEnum.REJECT.getTagType());
            }
        }
    }

    @Override
    public R<List<HistVo>> hist(String processInstanceId, String businessKey) {
        List<HistVo> result = new ArrayList<>();
        List<HistoricTaskInstance> histTask = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).processInstanceBusinessKey(businessKey).list();
        for (HistoricTaskInstance v : histTask) {
            HistVo histVo = new HistVo();
            histVo.setTackId(v.getId());
            histVo.setName(v.getName());
            // 0 完成 1进行中 duration
            histVo.setState(ObjUtil.isEmpty(v.getEndTime()) ? 1 : 0);
            // 执行者
            histVo.setLastUser(v.getAssignee());
            histVo.setStartTime(v.getStartTime());
            histVo.setEndTime(v.getEndTime());
            result.add(histVo);
        }
        result = result.stream().sorted(new Comparator<HistVo>() {
            @Override
            public int compare(HistVo o1, HistVo o2) {
                return Math.toIntExact(o1.getStartTime().getTime() - o2.getStartTime().getTime());
            }
        }).collect(Collectors.toList());
        return R.success(result);
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
