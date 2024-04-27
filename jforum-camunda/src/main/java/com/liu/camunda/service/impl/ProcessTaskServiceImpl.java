package com.liu.camunda.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.liu.camunda.constants.BpmConstants;
import com.liu.camunda.service.ProcessTaskService;
import com.liu.camunda.vo.CompleteTaskVo;
import com.liu.camunda.vo.TaskVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:53
 */
@Service
public class ProcessTaskServiceImpl implements ProcessTaskService {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public R<List<TaskVo>> singleToDoTaskList(SysUser user, String businessKey) {
        String userId = user.getUserId().toString();

        List<TaskVo> taskList = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey).list();
        for (Task task : tasks) {
            // 不是什么人都可以对流程进行审批的 这里 进行筛选
            List<String> userIds = null;
            try {
                userIds = (List<String>) taskService.getVariable(task.getId(), BpmConstants.CANDIDATE_USERS);
                // 符合 候选用户才行
                if (userIds.contains(userId)) {
                    taskToVo(taskList, task);
                }
            } catch (Exception ignored) {

            }
        }
        return R.success(taskList);
    }

    @Override
    public R<List<TaskVo>> multiToDoTaskList(SysUser user, String[] businessKeys) {
        String userId = user.getUserId().toString();
        List<TaskVo> taskList = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceBusinessKeyIn(businessKeys)
                .taskAssignee(userId)
                .list();
        taskToVo(taskList, tasks);
        return R.success(taskList);
    }

    @Override
    public R<List<TaskVo>> pageToDoTask(SysUser user, String businessKey, Integer pageNum, Integer pageSize) {
        String userId = user.getUserId().toString();
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskAssignee(userId)
                // 设置分页参数
                .listPage((pageNum - 1) * pageSize, pageSize);
        List<TaskVo> taskList = new ArrayList<>();
        taskToVo(taskList, tasks);
        return R.success(taskList);
    }

    @Override
    public R<Map<String, String>> completeSingleTask(CompleteTaskVo requestParam) {
        Task task = checkTask(requestParam.getProcessInstanceId(), requestParam.getTaskId());
        Map<String, String> map = MapUtil.newHashMap(4);
        map.put("节点ID", task.getTaskDefinitionKey());
        map.put("节点名称", task.getName());
        taskService.complete(requestParam.getTaskId());
        return R.success(map);
    }

    @Override
    public R<List<TaskVo>> listDoneTask(SysUser user, String businessKey) {
        String userId = user.getUserId().toString();
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .taskAssignee(userId)
                .finished()
                .taskDeleteReason("completed")
                .list();
        List<TaskVo> taskList = new ArrayList<>();
        if (CollUtil.isNotEmpty(tasks)) {
            TaskVo taskVo;
            for (HistoricTaskInstance task : tasks) {
                taskVo = new TaskVo();
                taskVo.setProcessInstanceId(task.getProcessInstanceId());
                taskVo.setTaskId(task.getId());
                taskList.add(taskVo);
            }
        }
        return R.success(taskList);
    }


    /**
     * 原 Task 赋值 给 ListVo  因为Task是接口 如果直接返回会有序列化问题
     *
     * @param taskList ListVo集合
     * @param tasks    原数据
     */
    private void taskToVo(List<TaskVo> taskList, final List<Task> tasks) {
        if (CollUtil.isNotEmpty(tasks)) {
            TaskVo taskVo;
            for (Task task : tasks) {
                taskVo = new TaskVo();
                taskVo.setProcessInstanceId(task.getProcessInstanceId());
                taskVo.setTaskId(task.getId());
                taskList.add(taskVo);
            }
        }
    }

    /**
     * @param taskList ListVo集合
     * @param task     原数据
     */
    private void taskToVo(List<TaskVo> taskList, final Task task) {
        TaskVo taskVo;
        taskVo = new TaskVo();
        taskVo.setProcessInstanceId(task.getProcessInstanceId());
        taskVo.setTaskId(task.getId());
        taskList.add(taskVo);
    }


    /**
     * 检查任务是否存在
     *
     * @param processInstanceId 流程实例id
     * @param taskId            任务id
     * @return task
     */
    private Task checkTask(String processInstanceId, String taskId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskId(taskId).singleResult();
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        return task;
    }
}
