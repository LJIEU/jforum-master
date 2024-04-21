package com.liu.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: 任务监听器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 23:02
 */
@Component
public class FlowTaskListener implements TaskListener {
    public static final Logger log = LoggerFactory.getLogger(FlowTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("任务监听器 -----> {}", delegateTask.getEventName());
    }
}
