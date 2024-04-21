package com.liu.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: 委托
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 22:58
 */
@Component
public class SendDelegate implements JavaDelegate {
    public static final Logger log = LoggerFactory.getLogger(SendDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String initiator = (String) execution.getVariable("initiator");
        String processInstanceId = execution.getProcessInstanceId();
        log.info(initiator, "-----发起审批流程[{}]", processInstanceId);
    }
}
