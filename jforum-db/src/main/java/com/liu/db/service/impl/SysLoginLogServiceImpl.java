package com.liu.db.service.impl;

import com.liu.db.entity.SysLoginLog;
import com.liu.db.mapper.SysLoginLogMapper;
import com.liu.db.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统访问记录实际业务层 sys_login_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper sysloginlogMapper;

    @Override
    public List<SysLoginLog> selectSysLoginLogList(SysLoginLog sysloginlog) {
        return sysloginlogMapper.selectSysLoginLogList(sysloginlog);
    }

    @Override
    public SysLoginLog selectSysLoginLogByLoginId(Long loginId) {
        return sysloginlogMapper.selectSysLoginLogByLoginId(loginId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysLoginLog sysloginlog) {
        return sysloginlogMapper.insert(sysloginlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysLoginLog sysloginlog) {
        return sysloginlogMapper.update(sysloginlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] loginIds) {
        return sysloginlogMapper.deleteById(loginIds);
    }
}
