package com.liu.camunda.delegate;

import com.liu.camunda.constants.BpmConstants;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 违规积分核查
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/23 8:03
 */
@Component(value = BpmConstants.DELEGATE_VIOLATION_CHECKER)
public class ViolationChecker implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(ViolationChecker.class);

    @Resource
    private TaskService taskService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("查询积分");
        // 查询 发起者用户ID
        String initiator = (String) execution.getVariable(BpmConstants.INITIATOR);
        // 去查询该用户的违规记录  如果大于 阈值则走人工审核
        // TODO 2024/4/27/10:21 由于这里没有创建 积分表 所以这里模拟一下
//        int integral = RandomUtil.randomInt(0, 100);
        int integral = 99;
        if (integral > 50) {
            execution.setVariable(BpmConstants.NEED_USER, "true");
            // 将其 分配给 审批员
            List<String> list = new ArrayList<>();
//            // 全部 审核员1
            list.add("1");
            list.add("2");
            list.add("3");
            execution.setVariable(BpmConstants.CANDIDATE_USERS, list);
        } else {
            execution.setVariable(BpmConstants.NEED_USER, "false");
            // 修改 帖子 状态为 发布
            log.info("积分良好 ====> 成功发布");
        }
        log.info("___________违规积分检查器结束___________");
    }
}
