package com.liu.camunda.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Description: 委托
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/20 22:58
 */
@Slf4j
@Component
public class SendDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String initiator = (String) execution.getVariable("initiator");
        String processInstanceId = execution.getProcessInstanceId();
        log.info(initiator, "-----发起审批流程[{}]", processInstanceId);
    }
}
