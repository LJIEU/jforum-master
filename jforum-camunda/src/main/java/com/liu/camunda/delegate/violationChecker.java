package com.liu.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Description: 违规积分核查
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/23 8:03
 */
@Component(value = "violationChecker")
public class violationChecker implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

    }
}
