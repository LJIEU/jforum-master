package com.liu.system.service.impl;

import com.liu.system.dao.SysOperateLog;
import com.liu.system.mapper.SysOperateLogMapper;
import com.liu.system.service.SysOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作日志实际业务层 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysOperateLogServiceImpl implements SysOperateLogService {

    @Autowired
    private SysOperateLogMapper sysoperatelogMapper;

    @Override
    public List<SysOperateLog> selectSysOperateLogList(SysOperateLog sysoperatelog)
    {
        return sysoperatelogMapper.selectSysOperateLogList(sysoperatelog);
    }

    @Override
    public SysOperateLog selectSysOperateLogByOperateId(Long operateId) {
        return sysoperatelogMapper.selectSysOperateLogByOperateId(operateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysOperateLog sysoperatelog)
    {
        return sysoperatelogMapper.insert(sysoperatelog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysOperateLog sysoperatelog)
    {
        return sysoperatelogMapper.update(sysoperatelog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] operateIds) {
        return sysoperatelogMapper.deleteById(operateIds);
    }
}
