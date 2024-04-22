package com.liu.camunda.service;

import com.liu.camunda.vo.RejectInstanceVo;
import com.liu.camunda.vo.StartProcessVo;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;

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
}
