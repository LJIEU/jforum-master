package com.liu.system.mapper;

import com.liu.system.dao.SysOperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志接口层 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Mapper
public interface SysOperateLogMapper {


    /**
     * 查询 操作日志 列表
     *
     * @param sysoperatelog 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysOperateLog> selectSysOperateLogList(SysOperateLog sysoperatelog);

    /**
     * 获取 操作日志 详细信息
     *
     * @param operateId 操作日志ID
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
     * 批量删除 操作日志
     *
     * @param operateIds 操作ID列表
     * @return 删除情况
     */
    int deleteById(@Param("operateIds") Long[] operateIds);
}
