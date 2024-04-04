package com.liu.system.service;

import com.liu.system.dao.SysConfig;

import java.util.List;

/**
 * 参数配置服务层 sys_config
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysConfigService {
    /**
     * 查询 参数配置 列表
     *
     * @param sysconfig 参数配置
     * @return 返回 分页结果
     */
    List<SysConfig> selectSysConfigList(SysConfig sysconfig);


    /**
     * 获取 参数配置 详细信息
     *
     * @param configId 参数主键
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
     * 删除 参数配置
     *
     * @param configIds 参数主键 列表
     * @return 删除情况
     */
    int delete(Integer[] configIds);


    /**
     * 根据 config_key 查询 config_value
     *
     * @param key 键
     * @return 值
     */
    String selectConfigByKey(String key);

    /**
     * 查询验证码是否开启
     */
    boolean selectCaptchaEnabled();
}
