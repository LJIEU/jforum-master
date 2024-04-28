package com.liu.camunda.service.impl;

import cn.hutool.core.map.MapUtil;
import com.liu.camunda.constants.BpmConstants;
import com.liu.camunda.service.BpmPostService;
import com.liu.camunda.vo.BpmPostVo;
import com.liu.camunda.vo.SubmitPostVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.Post;
import com.liu.db.entity.SysUser;
import com.liu.db.service.PostService;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private static final Logger log = LoggerFactory.getLogger(BpmPostServiceImpl.class);
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @SuppressWarnings("AlibabaRemoveCommentedCode")
    @Override
    public R<Map<String, Object>> startPostFlow(BpmPostVo bpmPostVo, SysUser user) {
        // 获取流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(bpmPostVo.getProcessDefinitionId())
                .singleResult();
        if (processDefinition == null) {
            throw new ServiceException("未找到流程定义！");
        }
        // 创建流程实例
        ProcessInstantiationBuilder processInstantiationBuilder =
                runtimeService.createProcessInstanceByKey(processDefinition.getKey())
                        .businessKey(BpmConstants.POST_BUSINESS);

        // 设置 流程发起者ID 后期使用 getStartUserId()
        identityService.setAuthenticatedUserId(user.getUserId().toString());

        // 如果有需要传递的变量，可以在此设置  设置 发起者信息 ==》 当前用户ID
        processInstantiationBuilder.setVariable(BpmConstants.INITIATOR, user.getUserId().toString());
        processInstantiationBuilder.setVariable(BpmConstants.POST_ID, bpmPostVo.getPostId());
        // 以上代码可以写以下格式 设置变量
//        runtimeService.startProcessInstanceByKey("key", "businessKey", new HashMap<>());

        // 启动流程实例
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

        // TODO 2024/4/27/15:06 这里进行自动发起 审查
        this.initiateReview(processInstance.getId(), user);
        return R.success(result);
    }

    @Override
    public R<String> delete(String processInstanceId) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, "删除~");
        } catch (Exception ignored) {

        }
        return R.success();
    }

    @Override
    public R<String> initiateReview(String processInstanceId, SysUser user) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        // 该任务是由我发起的
        task.setAssignee(user.getUserId().toString());
        taskService.complete(task.getId());
        return R.success();
    }

    @Override
    public R<String> reviewing(SubmitPostVo submitPostVo, SysUser user) {
        // TODO 2024/4/28/22:19 一定要对数据进行校验后再进行 下面的流程
        Task task = taskService.createTaskQuery().processInstanceId(submitPostVo.getProcessInstanceId()).singleResult();
        List<HistoricVariableInstance> variableInstanceList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(submitPostVo.getProcessInstanceId()).list();
        // 发起者ID
        String initiator = "";
        // 帖子ID
        String postId = "";
        // 可以进行审核的 用户ID列表
        List<String> candidateUsers = new ArrayList<String>();
        for (HistoricVariableInstance variableInstance : variableInstanceList) {
            if (variableInstance.getName().equals(BpmConstants.INITIATOR)) {
                initiator = (String) variableInstance.getValue();
            }
            if (variableInstance.getName().equals(BpmConstants.POST_ID)) {
                postId = (String) variableInstance.getValue();
            }
            if (variableInstance.getName().equals(BpmConstants.CANDIDATE_USERS)) {
                candidateUsers = (List<String>) variableInstance.getValue();
            }
        }
        // TODO 2024/4/27/11:28 这里进行模拟 后期是 只能当前用户才能进行审核
        if (candidateUsers.stream().noneMatch(v -> user.getUserId().toString().equals(v))) {
            throw new ServiceException("你无权限审核!");
        }

        log.info("审核信息::发起者:{} ==> 帖子:{} ==> 可审核人员:{}", initiator, postId, candidateUsers);

        // 设置 审批意见  和 审批选项[0通过  1不通过]
        taskService.setVariable(task.getId(), BpmConstants.OPTIONS, submitPostVo.getOptions());
        taskService.setVariable(task.getId(), BpmConstants.OPINION, submitPostVo.getOpinion());

        // 在驳回时 要将 一些变量 删除  其实通过之后也可以删除该变量
        runtimeService.removeVariable(submitPostVo.getProcessInstanceId(), BpmConstants.CANDIDATE_USERS);
        // 设置候选人 为自己   ==>即 表示是我完成的
        taskService.setAssignee(task.getId(), user.getUserId().toString());
        taskService.complete(task.getId());
        return R.success();
    }

    @Override
    public R<Map<String, Object>> getInfo(String processInstanceId) {
        String postId = (String) historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).variableName(BpmConstants.POST_ID).singleResult().getValue();
        Post post = SpringUtils.getBean(PostService.class).selectPostByPostId(postId);
        if (post == null) {
            return R.success();
        }
        Map<String, Object> result = MapUtil.newHashMap();
        result.put("postId", postId);
        result.put("title", post.getTitle());
        result.put("content", post.getContent());
        return R.success(result);
    }

}
