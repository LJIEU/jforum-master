package com.liu.camunda.service;

import com.liu.camunda.vo.DefinitionVo;
import com.liu.camunda.vo.DeployVo;
import com.liu.camunda.vo.FormVo;
import com.liu.core.result.R;
import com.liu.db.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/21 17:07
 */
public interface ProcessDefinitionService {

    /**
     * 部署
     *
     * @param user     用户信息
     * @param file     参数
     * @param bpmnName 流程名称
     * @return 返回结果
     */
    R<Map<String, Object>> deploy(SysUser user, MultipartFile file, String bpmnName);


    /**
     * 获取 Bpmn模型 实例
     *
     * @param processDefinitionId 流程定义ID
     * @return 返回结果
     */
    R<String> getBpmnModelInstance(String processDefinitionId);

    /**
     * 删除 部署
     *
     * @param deploymentId 部署ID
     * @return 返回结果
     */
    R<String> deleteDeployment(String deploymentId);

    /**
     * 挂起/暂停 流程定义
     *
     * @param processDefinitionId 流程定义
     * @return 返回结果
     */
    R<String> suspendProcessDefinitionById(String processDefinitionId);

    /**
     * JSON ==> Bpmn
     *
     * @param param 流程定义数据
     * @return 返回结果
     */
    R<String> convert(DefinitionVo param);

    /**
     * 通过XML部署流程
     *
     * @param user     操作用户
     * @param deployVo 参数
     * @return 返回结果
     */
    R<Map<String, Object>> deployXml(SysUser user, DeployVo deployVo);

    /**
     * 获取部署流程信息
     * @return 返回结果
     */
    R<List<DeployVo>> deployList();

    /**
     * 获取表单数据
     * @param deploymentId 部署ID
     * @return 返回结果
     */
    R<Map<String, Object>> formDataHtml(String deploymentId);
}
