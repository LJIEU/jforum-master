package com.liu.system.service;

import com.liu.system.dao.SysLoginLog;

import java.util.List;

/**
 * 系统访问记录服务层 sys_login_log
 *
 * @author JIE
 * @since 2024-04-03
 */
public interface SysLoginLogService {
    /**
     * 查询 系统访问记录 列表
     *
     * @param sysloginlog 系统访问记录
     * @return 返回 分页结果
     */
    List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysloginlog);


    /**
     * 获取 系统访问记录 详细信息
     *
     * @param loginId 访问ID
     * @return 返回系统访问记录信息
     */
    SysLoginLog selectSysLoginLogByLoginId(Long loginId);

    /**
     * 新增 系统访问记录
     *
     * @param sysloginlog 系统访问记录
     * @return 添加情况
     */
    int insert(SysLoginLog sysloginlog);

    /**
     * 修改 系统访问记录
     *
     * @param sysloginlog 系统访问记录
     * @return 修改情况
     */
    int update(SysLoginLog sysloginlog);

    /**
     * 删除 系统访问记录
     *
     * @param loginIds 访问ID 列表
     * @return 删除情况
     */
    int delete(Long[] loginIds);

}
