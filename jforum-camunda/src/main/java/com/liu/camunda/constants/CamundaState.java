package com.liu.camunda.constants;

/**
 * Description: 状态
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/27 17:41
 */
public interface CamundaState {
    /**
     * 进行中
     */
    String ACTIVE = "ACTIVE";
    /**
     * 撤销
     */
    String INTERNALLY_TERMINATED = "INTERNALLY_TERMINATED";
    /**
     * 已完成
     */
    String COMPLETED = "COMPLETED";
}
