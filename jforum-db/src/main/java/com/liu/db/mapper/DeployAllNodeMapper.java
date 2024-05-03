package com.liu.db.mapper;

import com.liu.camunda.domin.BpmnInfo;
import com.liu.core.config.dynamic.DataSource;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.db.entity.DeployAllNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/03 14:41
 */
@DataSource(DataSourceType.MASTER)
@Mapper
public interface DeployAllNodeMapper {
    /**
     * 插入数据
     *
     * @param deployId 部署ID
     * @param data     整理好的节点数据
     */
    void insert(@Param("deployId") String deployId, @Param("data") List<BpmnInfo> data);


    /**
     * 获取数据
     *
     * @param deployId 部署ID
     * @return 返回数据
     */
    DeployAllNode getData(String deployId);

    /**
     * 删除
     *
     * @param deploymentId 部署ID
     */
    void delete(String deploymentId);
}
