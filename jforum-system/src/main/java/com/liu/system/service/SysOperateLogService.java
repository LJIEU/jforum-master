package com.liu.system.service;

import com.liu.system.dao.SysOperateLog;

import java.util.List;

/**
 * 操作日志服务层 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysOperateLogService {
    /**
     * 查询 操作日志 列表
     *
     * @param sysoperatelog 操作日志
     * @return 返回 分页结果
     */
    List<SysOperateLog> selectSysOperateLogList(SysOperateLog sysoperatelog);


    /**
     * 获取 操作日志 详细信息
     *
     * @param operateId 操作ID
     * @return 返回操作日志信息
     */
        SysOperateLog selectSysOperateLogByOperateId(Long operateId);

    /**
     * 新增 操作日志
     *
     * @param sysoperatelog 操作日志
     * @return 添加情况
     */
    int insert(SysOperateLog sysoperatelog);

    /**
     * 修改 操作日志
     *
     * @param sysoperatelog 操作日志
     * @return 修改情况
     */
    int update(SysOperateLog sysoperatelog);

    /**
     * 删除 操作日志
     *
     * @param operateIds 操作ID 列表
     * @return 删除情况
     */
    int delete(Long[] operateIds);
}
