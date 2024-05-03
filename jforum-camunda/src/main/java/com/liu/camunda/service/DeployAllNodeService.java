package com.liu.camunda.service;

import com.liu.camunda.domin.BpmnInfo;
import com.liu.core.excption.ServiceException;
import com.liu.camunda.utils.CamundaUtils;
import com.liu.db.entity.DeployAllNode;
import com.liu.db.mapper.DeployAllNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/03 14:40
 */
@Service
public class DeployAllNodeService {
    @Autowired
    private DeployAllNodeMapper deployAllNodeMapper;

    @Transactional(rollbackFor = Exception.class)
    public void insert(String deployId, byte[] bpmnXmlByte) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new ByteArrayInputStream(bpmnXmlByte));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new ServiceException("解析异常!");
        }

        // 所有节点
        List<BpmnInfo> bpmnInfoList = CamundaUtils.foreachBpmn(doc.getDocumentElement());
        // 整理好的 节点
        List<BpmnInfo> result = CamundaUtils.sequenceNode(bpmnInfoList);
        deployAllNodeMapper.insert(deployId, result);
    }

    public List<BpmnInfo> selectByDeployId(String deployId) {
        DeployAllNode deployAllNode = deployAllNodeMapper.getData(deployId);
        return deployAllNode.getData();
    }


    public void delete(String deploymentId) {
        deployAllNodeMapper.delete(deploymentId);
    }
}
