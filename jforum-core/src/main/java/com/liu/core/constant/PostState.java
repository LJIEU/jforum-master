package com.liu.core.constant;

/**
 * Description:  帖子状态
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/16 16:58
 */
public interface PostState {
    /**
     * 隐私 默认
     */
    String POST_PRIVACY = "0";
    /**
     * 公布
     */
    String POST_PUBLISH = "1";
    /**
     * 待审核
     */
    String POST_PENDING = "2";
    /**
     * 审核中
     */
    String POST_REVIEWING = "3";
    /**
     * 驳回
     */
    String POST_REJECT = "5";
}
