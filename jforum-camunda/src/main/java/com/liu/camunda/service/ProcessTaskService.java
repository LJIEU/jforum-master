package com.liu.camunda.service;

import com.liu.camunda.vo.CompleteTaskVo;
import com.liu.camunda.vo.TaskVo;
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
public interface ProcessTaskService {
    /**
     * 查询当前用户的待办任务--针对单个业务
     *
     * @param user        用户信息
     * @param businessKey 业务key
     * @return 待办任务信息
     */
    R<List<TaskVo>> singleToDoTaskList(SysUser user, String businessKey);

    /**
     * 查询当前用户的待办任务--针对多个业务
     *
     * @param user         用户信息
     * @param businessKeys 业务key
     * @return 待办任务信息
     */
    R<List<TaskVo>> multiToDoTaskList(SysUser user, String[] businessKeys);

    /**
     * 分页查询用户指定业务的待办任务
     *
     * @param user        用户信息
     * @param businessKey 业务key
     * @param pageNum     页码
     * @param pageSize    数量
     * @return 待办任务信息
     */
    R<List<TaskVo>> pageToDoTask(SysUser user, String businessKey, Integer pageNum, Integer pageSize);

    /**
     * 完成单个任务
     *
     * @param requestParam 请求参数
     * @return 任务所在节点信息
     */
    R<Map<String, String>> completeSingleTask(CompleteTaskVo requestParam);


    /**
     * 查询用户已办任务
     *
     * @param businessKey 业务Key
     * @return 流程实例id
     */
    R<List<TaskVo>> listDoneTask(SysUser user, String businessKey);
}
