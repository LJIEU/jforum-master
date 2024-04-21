package com.liu.camunda.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * Description: 执行监听器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 23:05
 */
@Slf4j
@Component
public class FlowExecutionListener implements ExecutionListener {
    @Resource
    private RepositoryService repositoryService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        log.info("执行监听器 -----> " + execution.getEventName());
    }
}
