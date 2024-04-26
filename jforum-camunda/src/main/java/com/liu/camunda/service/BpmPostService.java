package com.liu.camunda.service;

import com.liu.camunda.vo.BpmPostVo;
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
}
