package com.liu.camunda.service;

import com.liu.camunda.vo.SequentialAddVo;
import com.liu.core.result.R;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 18:56
 */
public interface ExtendService {
    /**
     * 添加 流程
     * @param requestParam 请求参数
     */
    R<String> addAssignee(SequentialAddVo requestParam);
}
