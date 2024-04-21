package com.liu.db.service.impl;

import com.liu.core.service.BaseConfigService;
import com.liu.db.entity.SysConfig;
import com.liu.db.mapper.SysConfigMapper;
import com.liu.db.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 参数配置实际业务层 sys_config
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysConfigServiceImpl extends BaseConfigService implements SysConfigService {

    @Autowired
    private SysConfigMapper sysconfigMapper;

    @Override
    public List<SysConfig> selectSysConfigList(SysConfig sysconfig) {
        return sysconfigMapper.selectSysConfigList(sysconfig);
    }

    @Override
    public SysConfig selectSysConfigByConfigId(Integer configId) {
        return sysconfigMapper.selectSysConfigByConfigId(configId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysConfig sysconfig) {
        return sysconfigMapper.insert(sysconfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysConfig sysconfig) {
        return sysconfigMapper.update(sysconfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Integer[] configIds) {
        return sysconfigMapper.deleteById(configIds);
    }

    @Override
    public String selectConfigByKey(String key) {
        return sysconfigMapper.selectConfigByKey(key);
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String value = sysconfigMapper.selectConfigByKey("sys.account.captchaEnabled");
        return value.equalsIgnoreCase("true");
    }
}
