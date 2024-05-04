package com.liu.camunda.constants;

/**
 * Description: 常量
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/27 9:47
 */
public interface BpmnConstants {
    /**
     * 发起者
     */
    String INITIATOR = "initiator";
    /**
     * 帖子ID
     */
    String POST_ID = "postId";
    /**
     * 积分查询机
     */
    String DELEGATE_VIOLATION_CHECKER = "violationChecker";
    /**
     * 需要人工审核  ==》 true人工   false直接结束
     */
    String NEED_USER = "needUser";
    /**
     * 可以进行审核的人员ID
     */
    String CANDIDATE_USERS = "candidateUsers";
    /**
     * 审核选项 '0'通过  '1'驳回
     */
    String OPTIONS = "options";
    /**
     * 审核意见
     */
    String OPINION = "opinion";
    /**
     * 业务标识 key
     */
    String POST_BUSINESS = "post_flow";


}
