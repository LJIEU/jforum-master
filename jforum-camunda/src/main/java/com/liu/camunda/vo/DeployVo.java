package com.liu.camunda.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:18
 */
@Schema(name = "部署信息")
public class DeployVo {

    /**
     * 名称
     */
    @Schema(description = "Bpmn流程图名称")
    private String bpmnName;

    /**
     * 需要部署的bpmn文件
     */
    @Schema(description = "Bpmn流程图")
    private MultipartFile file;

    public String getBpmnName() {
        return bpmnName;
    }

    public void setBpmnName(String bpmnName) {
        this.bpmnName = bpmnName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
