package com.liu.camunda.service.impl;

import com.liu.camunda.service.ProcessInstanceService;
import com.liu.camunda.vo.RejectInstanceVo;
import com.liu.camunda.vo.StartProcessVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:53
 */
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;


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
}
