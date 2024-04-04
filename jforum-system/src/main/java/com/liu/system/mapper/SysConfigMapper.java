package com.liu.system.mapper;

import com.liu.system.dao.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数配置接口层 sys_config
 *
 * @author JIE
 * @since 2024-04-03
 */
@Mapper
public interface SysConfigMapper {


    /**
     * 查询 参数配置 列表
     *
     * @param sysconfig 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysConfig> selectSysConfigList(SysConfig sysconfig);

    /**
     * 获取 参数配置 详细信息
     *
     * @param configId 参数配置ID
     * @return 返回参数配置信息
     */
    SysConfig selectSysConfigByConfigId(Integer configId);

    /**
     * 新增 参数配置
     *
     * @param sysconfig 参数配置
     * @return 添加情况
     */
    int insert(SysConfig sysconfig);

    /**
     * 修改 参数配置
     *
     * @param sysconfig 参数配置
     * @return 修改情况
     */
    int update(SysConfig sysconfig);

    /**
     * 批量删除 参数配置
     *
     * @param configIds 参数主键列表
     * @return 删除情况
     */
    int deleteById(@Param("configIds") Integer[] configIds);


    /**
     * 根据 config_key 查询 config_value
     * @param key 键
     * @return 值
     */
    String selectConfigByKey(String key);
}
