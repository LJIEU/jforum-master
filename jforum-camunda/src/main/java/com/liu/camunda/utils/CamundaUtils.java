package com.liu.camunda.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.camunda.constants.BpmnConstants;
import com.liu.camunda.domin.BpmnInfo;
import com.liu.camunda.domin.FormField;
import com.liu.camunda.vo.FormVo;
import com.liu.camunda.vo.RulesVo;
import com.liu.core.constant.HTMLConstants;
import com.liu.core.excption.ServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/01 13:15
 */
public class CamundaUtils {
    // 解读 Camunda 流程  走向
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // 创建 DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 将字符串解析为 Document
        Document doc = builder.parse(new File("C:\\Users\\lj147\\Desktop\\jforum-master\\jforum-system\\src\\main\\resources\\bpmn\\post_flow.bpmn"));

        List<BpmnInfo> bpmnInfoList = foreachBpmn(doc.getDocumentElement());
//        bpmnInfoList.forEach(System.out::println);
        System.out.println("================================");
        List<BpmnInfo> resultBpmn = sequenceNode(bpmnInfoList);
        resultBpmn.forEach(System.out::println);

        System.out.println("================================");
        List<FormVo> formVoList = new ArrayList<>();
        List<RulesVo> rulesVoList = new ArrayList<>();
        resultBpmn.stream().filter(v -> {
            if (v.getFormData() == null) {
                return false;
            } else {
                for (BpmnInfo bpmnInfo : resultBpmn) {
                    // 过滤 驳回的
                    if (bpmnInfo != v && v.getId().equals(bpmnInfo.getId()) && v.getDesc() != null) {
                        return false;
                    }
                }
            }
            return true;
        }).peek(v -> {
            Map<String, Object> formDataResult = formDataResult(v.getFormData(), resultBpmn);
            formDataResult.forEach((k, b) -> {
                System.out.println(k + "\t" + b);
            });
        }).collect(Collectors.toList());

        System.out.println("===========================");
        formVoList.forEach(System.out::println);
    }

    public static Map<String, Object> formDataResult(List<FormField> formData, List<BpmnInfo> resultBpmn) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        List<FormVo> formVoList = new ArrayList<>();
        List<RulesVo> rulesVoList = new ArrayList<>();
        for (FormField field : formData) {
            FormVo formVo = new FormVo();
            RulesVo rulesVo = new RulesVo();
            // 去判断该字段的类型
            // 默认
            formVo.setType(HTMLConstants.INPUT);
            formVo.setProp(field.getId());
            formVo.setLabel(field.getLabel());
            rulesVo.setField(field.getId());
            rulesVo.setMessage("请输入" + field.getLabel());
            rulesVo.setTrigger("blur");
            // 如果是意见 那就使用多行文本
            if (field.getId().equals(BpmnConstants.OPINION)) {
                formVo.setType(HTMLConstants.TEXTAREA);
            }
            // 去判断类型 1.默认的  2.查看 conditionExpression 是否有该变量存在
            List<FormVo.MyOption> myOptions = new ArrayList<>();
            for (BpmnInfo bpmnInfo : resultBpmn) {
                if (bpmnInfo.getConditionExpression() != null &&
                        bpmnInfo.getConditionExpression().matches(".*" + field.getId() + ".*")) {
                    // 如果存在 那说明是下拉选项 进行收集[值+name]
                    Matcher matcher = Pattern.compile("==\\s*(.*)}").matcher(bpmnInfo.getConditionExpression());
                    if (matcher.find()) {
                        String s = matcher.group(1);
                        if ("string".equals(field.getType())) {
                            s = s.replaceAll("'", "");
                            FormVo.MyOption myOption = new FormVo.MyOption();
                            myOption.setLabel(bpmnInfo.getDesc());
                            myOption.setValue(s);
                            myOptions.add(myOption);
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(myOptions)) {
                formVo.setType(HTMLConstants.SELECT);
                formVo.setOptions(myOptions);
                rulesVo.setMessage("请选择" + field.getLabel());
                rulesVo.setTrigger("changer");
            }

            formVoList.add(formVo);
            rulesVoList.add(rulesVo);
        }
        result.put("formData", formVoList);
        result.put("rules", rulesVoList);
        return result;
    }

    /**
     * 开启流程
     */
    public void start() {

    }

    /**
     * 整理 数据节点信息
     *
     * @param bpmnInfoList 源数据所有节点列表
     * @return 返回整理好的节点
     */
    public static List<BpmnInfo> sequenceNode(List<BpmnInfo> bpmnInfoList) {
        List<BpmnInfo> sequenceList = new ArrayList<>();
        // 计算节点数量
        int count = 0;
        for (BpmnInfo v : bpmnInfoList) {
            if (v.getUserTask() != null || v.getServiceTask() != null
                    || v.getExclusiveGateway() != null || v.getStartEvent() != null
                    || v.getEndEvent() != null) {
                if (v.getStartEvent() != null) {
                    sequenceList.add(v);
                }
                count++;
            }
        }
        if (sequenceList.size() != 1) {
            throw new ServiceException("无有效开始节点!可能包含多个或者少于1个");
        }
        // 节点顺序
        while (sequenceList.size() <= count) {
            BpmnInfo start = sequenceList.get(sequenceList.size() - 1);
            // 查询下一个节点
            for (String outgoing : start.getOutgoing()) {
                AtomicBoolean flag = new AtomicBoolean(false);
                AtomicReference<String> desc = new AtomicReference<>("");
                AtomicReference<String> conditionExpression = new AtomicReference<>("");
                String targetId = bpmnInfoList.stream().filter(v -> {
                    if (v.getSequenceFlow() != null && v.getId().equals(outgoing)) {
                        if (StrUtil.isNotEmpty(v.getName())) {
                            flag.set(true);
                            desc.set(v.getName());
                            conditionExpression.set(v.getConditionExpression());
//                            System.out.println(v.getName());
                        }
                        return true;
                    }
                    return false;
                }).map(BpmnInfo::getTargetRef).collect(Collectors.joining(","));
//                System.out.println(targetId);
                // 根据这个 targetId 就可以找到下一个节点 [如果后面又出现之前有的ID那就是驳回]
                for (BpmnInfo bpmnInfo : bpmnInfoList) {
                    if (bpmnInfo.getId().equals(targetId)) {
                        if (flag.get()) {
                            BpmnInfo copy = new BpmnInfo();
                            BeanUtil.copyProperties(bpmnInfo, copy);
                            copy.setDesc(desc.get());
                            copy.setConditionExpression(conditionExpression.get());
                            sequenceList.add(copy);
                        } else {
                            sequenceList.add(bpmnInfo);
                        }
                    }
                }
            }
        }
//        sequenceList.forEach(System.out::println);
        return sequenceList;
    }

    /**
     * 遍历 BPMN 文件
     *
     * @param node 节点
     * @return 获取所有节点信息
     */
    @SuppressWarnings({"AlibabaMethodTooLong", "AlibabaSwitchStatement", "EnhancedSwitchMigration", "AlibabaUndefineMagicConstant"})
    public static List<BpmnInfo> foreachBpmn(Node node) {
        Stack<BpmnInfo> stack = new Stack<>();
        // 父标签
        List<String> parentNodeName = List.of("bpmn:startEvent", "bpmn:serviceTask", "bpmn:userTask", "bpmn:exclusiveGateway", "bpmn:sequenceFlow", "bpmn:endEvent");
        // 子标签
        List<String> childNodeName = List.of("bpmn:outgoing", "camunda:outputParameter", "bpmn:incoming", "bpmn:conditionExpression");
        // 排除的标签
        List<String> excludeNodeName = List.of("bpmn:definitions", "bpmn:process", "bpmn:extensionElements", "camunda:inputOutput", "camunda:formData", "bpmndi:BPMNDiagram");
        while (node != null) {
            if ("bpmndi:BPMNDiagram".equals(node.getNodeName())) {
                break;
            }
            // 为父标签就创建对象
            if (parentNodeName.contains(node.getNodeName())) {
//                System.out.println("============" + node.getNodeName() + "=============");
                BpmnInfo bpmnInfo = new BpmnInfo();
                stack.push(bpmnInfo);
            }
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null && parentNodeName.contains(node.getNodeName())) {
                // 获取节点属性
                BpmnInfo info = stack.pop();
                switch (node.getNodeName()) {
                    case "bpmn:serviceTask":
                        info.setServiceTask(true);
                        break;
                    case "bpmn:userTask":
                        info.setUserTask(true);
                        break;
                    case "bpmn:startEvent":
                        info.setStartEvent(true);
                        break;
                    case "bpmn:endEvent":
                        info.setEndEvent(true);
                        break;
                    case "bpmn:sequenceFlow":
                        info.setSequenceFlow(true);
                        break;
                    case "bpmn:exclusiveGateway":
                        info.setExclusiveGateway(true);
                        break;
                }
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node item = attributes.item(i);
                    if (item != null) {
                        switch (item.getNodeName()) {
                            case "id":
                                info.setId(item.getNodeValue());
                                break;
                            case "name":
                                info.setName(item.getNodeValue());
                                break;
                            case "sourceRef":
                                info.setSourceRef(item.getNodeValue());
                                break;
                            case "targetRef":
                                info.setTargetRef(item.getNodeValue());
                                break;
                        }
                    }
                }
                stack.push(info);
            }
            if (childNodeName.contains(node.getNodeName()) && stack.peek() != null) {
                String text = node.getTextContent().trim();
                if (StrUtil.isEmpty(text)) {
                    break;
                }
                int temp = text.length() - 1;
                while (text.charAt(temp) == '\n' || text.charAt(temp) == '\t') {
                    text = text.substring(0, temp--);
                }
                BpmnInfo info = stack.pop();
                switch (node.getNodeName()) {
                    case "bpmn:outgoing":
                        if (info.getOutgoing() == null) {
                            info.setOutgoing(new ArrayList<>());
                        }
                        List<String> outgoing = info.getOutgoing();
                        outgoing.add(text);
                        info.setOutgoing(outgoing);
                        break;
                    case "bpmn:incoming":
                        if (info.getIncoming() == null) {
                            info.setIncoming(new ArrayList<>());
                        }
                        List<String> incoming = info.getIncoming();
                        incoming.add(text);
                        info.setIncoming(incoming);
                        break;
                    case "bpmn:conditionExpression":
                        info.setConditionExpression(text);
                        break;
                }
                stack.push(info);
            } else if ("camunda:formField".equals(node.getNodeName())) {
                // 如果是表单
                BpmnInfo info = stack.pop();
                List<FormField> formData = info.getFormData();
                FormField formField = new FormField();
                NamedNodeMap nodeAttributes = node.getAttributes();
                formField.setId(nodeAttributes.getNamedItem("id").getNodeValue());
                formField.setLabel(nodeAttributes.getNamedItem("label").getNodeValue());
                formField.setType(nodeAttributes.getNamedItem("type").getNodeValue());
                if (formData == null) {
                    formData = new ArrayList<>();
                }
                formData.add(formField);
                info.setFormData(formData);
                stack.push(info);
            }
            NodeList childNodes = node.getChildNodes();
            // 设置下一个节点为当前节点的第一个子节点
            if (childNodes.getLength() > 0) {
                node = childNodes.item(0);
            } else {
                // 如果没有子节点，则将当前节点设置为下一个同级节点
                Node sibling = node.getNextSibling();
                if (sibling != null) {
                    node = sibling;
                } else {
                    // 如果同级节点也没有了，则回溯到父节点的下一个同级节点
                    Node parent = node.getParentNode();
                    while (parent != null) {
                        sibling = parent.getNextSibling();
                        if (sibling != null) {
                            node = sibling;
                            break;
                        } else {
                            parent = parent.getParentNode();
                        }
                    }
                    // 如果没有找到下一个同级节点，则结束循环
                    if (parent == null) {
                        node = null;
                    }
                }
            }
        }
        return stack.stream().toList();
    }

    private static Map<String, Object> deepAll(Node node) {
        Map<String, Object> resultMap = new HashMap<>(16);
        // 获取属性
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null && !node.getNodeName().equals("bpmn:definitions")) {
            BpmnInfo bpmnInfo = new BpmnInfo();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                System.out.println(attribute.getNodeName() + "\t" + attribute.getNodeValue());
            }
            System.out.println(bpmnInfo);
        }
        // 获取子节点
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && !"bpmndi:BPMNDiagram".equals(child.getNodeName())) {
                // 递归
                Map<String, Object> childMap = deepAll(child);
                System.out.println("==========" + child.getNodeName() + "==========");
                resultMap.put(child.getNodeName(), childMap);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getTextContent().trim();
                if (!text.isEmpty()) {
//                    System.out.println(text);
                    resultMap.put("textContent", text);
                }
            }
        }
        return resultMap;
    }
}