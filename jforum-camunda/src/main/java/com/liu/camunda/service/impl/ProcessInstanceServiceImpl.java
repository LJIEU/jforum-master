package com.liu.camunda.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.camunda.constants.BpmConstants;
import com.liu.camunda.constants.CamundaState;
import com.liu.camunda.constants.StateEnum;
import com.liu.camunda.service.ProcessInstanceService;
import com.liu.camunda.vo.ProcessVo;
import com.liu.camunda.vo.RejectInstanceVo;
import com.liu.camunda.vo.StartProcessVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
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
                    processVo.setState(StateEnum.REJECT.getState());
                    processVo.setTagType(StateEnum.REJECT.getTagType());
                } else if (CamundaState.COMPLETED.equals(v.getState())) {
                    processVo.setState(StateEnum.COMPLETE.getState());
                    processVo.setTagType(StateEnum.COMPLETE.getTagType());
                }
                result.add(processVo);
            });
        }
        // 2. 我代办的流程
        List<Task> taskList = taskService.createTaskQuery().active().list();
        if (CollUtil.isNotEmpty(taskList)) {
            List<String> userIds = null;
            for (Task task : taskList) {
                if (StrUtil.isEmpty(task.getAssignee())) {
                    // 去查询变量
                    try {
                        userIds = (List<String>) taskService.getVariable(task.getId(), BpmConstants.CANDIDATE_USERS);
                        if (Objects.requireNonNull(userIds).contains(user.getUserId().toString())) {
                            result.add(taskToProcessVo(task));
                        }
                    } catch (Exception ignored) {

                    }
                } else if (task.getAssignee().equals(user.getUserId().toString())) {
                    result.add(taskToProcessVo(task));
                }
            }
        }
        // 3. 我完成的流程
        // TODO 2024/4/27/21:06 需要判断是否是驳回的流程
        List<HistoricTaskInstance> completedTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(user.getUserId().toString()).finished()
                .taskDeleteReason(CamundaState.COMPLETED.toLowerCase(Locale.ROOT)).list();
        for (HistoricTaskInstance completedTask : completedTasks) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(completedTask.getProcessInstanceId()).singleResult();
            ProcessVo processVo = toProcessVo(repositoryService.getProcessDefinition(completedTask.getProcessDefinitionId()).getName(),
                    completedTask.getProcessInstanceId(), historicProcessInstance.getBusinessKey(), StateEnum.COMPLETE, completedTask.getStartTime(),
                    false);
            result.add(processVo);
        }
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

        return processVo;
    }
}
