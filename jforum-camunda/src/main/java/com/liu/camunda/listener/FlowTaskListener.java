package com.liu.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * Description: 任务监听器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 23:02
 */
@Slf4j
@Component
public class FlowTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("任务监听器 -----> {}", delegateTask.getEventName());
    }
}
