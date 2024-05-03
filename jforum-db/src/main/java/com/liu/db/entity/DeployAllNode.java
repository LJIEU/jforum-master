package com.liu.db.entity;

import com.liu.camunda.domin.BpmnInfo;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/03 14:43
 */

@Schema(name = "部署BPMN的所有节点")
public class DeployAllNode extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 部署ID
     */
    @Schema(description = "部署ID")
    private String deployId;
    /**
     * 数据信息
     */
    @Schema(description = "数据信息")
    private List<BpmnInfo> data;

    public String getDeployId() {
        return deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

    public List<BpmnInfo> getData() {
        return data;
    }

    public void setData(List<BpmnInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeployAllNode{" +
                "deployId='" + deployId + '\'' +
                ", data=" + data +
                "} " + super.toString();
    }
}
