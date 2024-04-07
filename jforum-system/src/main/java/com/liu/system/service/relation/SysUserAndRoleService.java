package com.liu.system.service.relation;

import com.liu.system.dao.SysRole;
import com.liu.system.mapper.relation.SysUserAndRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 用户与角色关联业务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 11:40
 */
@Service
public class SysUserAndRoleService {
    @Autowired
    private SysUserAndRoleMapper userAndRoleMapper;

    public List<SysRole> getRoleByUserId(Long userId) {
        return userAndRoleMapper.getRoleByUserId(userId);
    }
}
