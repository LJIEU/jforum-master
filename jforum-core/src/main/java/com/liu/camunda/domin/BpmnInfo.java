package com.liu.camunda.domin;


import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/02 21:25
 */
public class BpmnInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -2294689118355368177L;
    private String id;
    private String name;
    private String desc;
    private List<String> outgoing;
    private List<String> incoming;
    private String sourceRef;
    private String targetRef;
    private String conditionExpression;
    private Boolean isServiceTask;
    private Boolean isUserTask;
    private Boolean isStartEvent;
    private Boolean isEndEvent;
    private Boolean isSequenceFlow;
    private Boolean isExclusiveGateway;
    private List<FormField> formData;

    @Override
    public String toString() {
        return "BpmnInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", outgoing=" + outgoing +
                ", incoming=" + incoming +
                ", sourceRef='" + sourceRef + '\'' +
                ", targetRef='" + targetRef + '\'' +
                ", conditionExpression='" + conditionExpression + '\'' +
                ", isServiceTask=" + isServiceTask +
                ", isUserTask=" + isUserTask +
                ", isStartEvent=" + isStartEvent +
                ", isEndEvent=" + isEndEvent +
                ", isSequenceFlow=" + isSequenceFlow +
                ", isExclusiveGateway=" + isExclusiveGateway +
                ", formData=" + formData +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<String> outgoing) {
        this.outgoing = outgoing;
    }

    public List<String> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<String> incoming) {
        this.incoming = incoming;
    }

    public Boolean getEndEvent() {
        return isEndEvent;
    }

    public void setEndEvent(Boolean endEvent) {
        isEndEvent = endEvent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public Boolean getServiceTask() {
        return isServiceTask;
    }

    public void setServiceTask(Boolean serviceTask) {
        isServiceTask = serviceTask;
    }

    public Boolean getUserTask() {
        return isUserTask;
    }

    public void setUserTask(Boolean userTask) {
        isUserTask = userTask;
    }

    public Boolean getStartEvent() {
        return isStartEvent;
    }

    public void setStartEvent(Boolean startEvent) {
        isStartEvent = startEvent;
    }

    public Boolean getSequenceFlow() {
        return isSequenceFlow;
    }

    public void setSequenceFlow(Boolean sequenceFlow) {
        isSequenceFlow = sequenceFlow;
    }

    public Boolean getExclusiveGateway() {
        return isExclusiveGateway;
    }

    public void setExclusiveGateway(Boolean exclusiveGateway) {
        isExclusiveGateway = exclusiveGateway;
    }

    public List<FormField> getFormData() {
        return formData;
    }

    public void setFormData(List<FormField> formData) {
        this.formData = formData;
    }
}
