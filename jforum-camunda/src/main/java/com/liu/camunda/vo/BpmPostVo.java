package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/26 16:10
 */
@Schema(name = "发起流程")
public class BpmPostVo {
    @Schema(description = "流程ID", defaultValue = "name:version:id")
    private String processDefinitionId;

    @Schema(description = "帖子ID", defaultValue = "009c199e04deb34331a6cc12")
    private String postId;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
