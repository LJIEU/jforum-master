package com.liu.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.liu.system.dao.SysRole;
import com.liu.system.mapper.SysRoleMapper;
import com.liu.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色信息实际业务层 sys_role
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysroleMapper;

    @Override
    public List<SysRole> selectSysRoleList(SysRole sysrole) {
        return sysroleMapper.selectSysRoleList(sysrole);
    }

    @Override
    public SysRole selectSysRoleByRoleId(Long roleId) {
        return sysroleMapper.selectSysRoleByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysRole sysrole) {
        return sysroleMapper.insert(sysrole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysRole sysrole) {
        return sysroleMapper.update(sysrole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] roleIds) {
        int count = 0;
        for (Long roleId : roleIds) {
            sysroleMapper.deleteById(roleId);
            count++;
        }
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long roleId, String status) {
        if (roleId != null) {
            if (StrUtil.isNotEmpty(status) && (status.equals("1") || status.equals("0"))) {
                sysroleMapper.updateStatus(roleId, status);
            }
        }
    }

}
