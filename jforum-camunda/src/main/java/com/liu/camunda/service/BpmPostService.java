package com.liu.camunda.service;

import com.liu.camunda.vo.BpmPostVo;
import com.liu.camunda.vo.SubmitPostVo;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;

import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/26 15:42
 */
public interface BpmPostService {
    /**
     * 启动流程
     *
     * @param bpmPostVo 参数
     * @param user      当前用户
     * @return 返回 流程定义 ID
     */
    R<Map<String, Object>> startPostFlow(BpmPostVo bpmPostVo, SysUser user);

    /**
     * 根据 流程实例ID 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 返回 结果
     */
    R<String> delete(String processInstanceId);

    /**
     * 发起审核流程
     *
     * @param processInstanceId 流程实例ID
     * @param user              当前用户
     * @return 返回 结果
     */
    R<String> initiateReview(String processInstanceId, SysUser user);

    /**
     * 进行审核
     *
     * @param submitPostVo 提交信息
     * @param user         当前审核员信息
     * @return 返回结果
     */
    R<String> reviewing(SubmitPostVo submitPostVo, SysUser user);

    /**
     * 获取 帖子信息
     *
     * @param processInstanceId 流程实例
     * @return 返回结果
     */
    R<Map<String, Object>> getInfo(String processInstanceId);

    /**
     * 获取 帖子对应的 实例ID
     * @param postId 帖子ID
     * @return 返回结果
     */
    String getInstanceId(String postId);

    /**
     * 驳回的流程 进行提交
     * @param instanceId 流程定义
     * @param user 用户
     */
    void submitPost(String instanceId, SysUser user);
}
