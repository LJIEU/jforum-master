package com.liu.camunda.service;

import com.liu.camunda.vo.HistVo;
import com.liu.camunda.vo.ProcessVo;
import com.liu.camunda.vo.RejectInstanceVo;
import com.liu.camunda.vo.StartProcessVo;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/22 12:53
 */
public interface ProcessInstanceService {
    /**
     * 创建流程实例
     *
     * @param user         用户信息
     * @param requestParam 流程定义参数
     * @return 返回结果
     */
    R<String> startProcessInstanceByKey(SysUser user, StartProcessVo requestParam);

    /**
     * 创建流程实例
     *
     * @param user         用户信息
     * @param requestParam 流程定义参数
     * @return 返回结果
     */
    R<String> startProcessInstanceById(SysUser user, StartProcessVo requestParam);


    /**
     * 驳回流程实例
     *
     * @param requestParam 请求参数
     * @return 是否驳回成功
     */
    R<String> rejectProcessInstance(RejectInstanceVo requestParam);


    /**
     * 获取当前用户的 审核全部记录
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param user     用户
     * @return 返回结果
     */
    R<List<ProcessVo>> list(Integer pageNum, Integer pageSize, SysUser user);

    /**
     * 获取流程实例的任务节点
     *
     * @param processInstanceId 流程实例
     * @param businessKey       流程业务Key
     * @return 返回结果
     */
    R<List<HistVo>> hist(String processInstanceId, String businessKey);

    /**
     * 启动流程
     *
     * @param deployId 部署ID
     * @param params   参数列表
     * @param user     用户
     * @return 返回结果
     */
    R<String> startProcessInstanceByDeployId(String deployId, Map<String, Object> params, SysUser user);

    /**
     * 根据流程实例ID 返回对应的表单
     *
     * @param instanceId 流程实例ID
     * @param user       用户
     * @return 返回结果
     */
    R<Map<String, Object>> formHtmlByInstanceId(String instanceId, SysUser user);

    /**
     * 提交当前节点任务
     *
     * @param instanceId 实例ID
     * @param user       用户
     * @param params     参数
     * @return 返回结果
     */
    R<String> complete(String instanceId, SysUser user, Map<String, Object> params);
}
