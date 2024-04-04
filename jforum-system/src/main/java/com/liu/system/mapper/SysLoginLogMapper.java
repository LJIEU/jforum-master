package com.liu.system.mapper;

import com.liu.system.dao.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统访问记录接口层 sys_login_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Mapper
public interface SysLoginLogMapper {


    /**
     * 查询 系统访问记录 列表
     *
     * @param sysloginlog 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysloginlog);

    /**
     * 获取 系统访问记录 详细信息
     *
     * @param loginId 系统访问记录ID
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
     * 批量删除 系统访问记录
     *
     * @param loginIds 访问ID列表
     * @return 删除情况
     */
    int deleteById(@Param("loginIds") Long[] loginIds);
}
