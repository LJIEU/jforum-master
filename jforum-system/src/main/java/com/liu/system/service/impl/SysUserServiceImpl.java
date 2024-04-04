package com.liu.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.liu.core.constant.Constants;
import com.liu.core.model.BaseUser;
import com.liu.security.service.BaseService;
import com.liu.system.constants.UserConstants;
import com.liu.system.dao.SysUser;
import com.liu.system.mapper.SysUserMapper;
import com.liu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户信息实际业务层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysUserServiceImpl implements SysUserService, BaseService<SysUser> {

    @Autowired
    private SysUserMapper sysuserMapper;

    @Override
    public List<SysUser> selectSysUserList(SysUser sysuser) {
        return sysuserMapper.selectSysUserList(sysuser);
    }

    @Override
    public SysUser selectSysUserByUserId(Long userId) {
        return sysuserMapper.selectSysUserByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysUser sysuser) {
        return sysuserMapper.insert(sysuser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysUser sysuser) {
        return sysuserMapper.update(sysuser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] userIds) {
        return sysuserMapper.deleteById(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(SysUser sysUser) {
        sysuserMapper.updateUserProfile(sysUser);
    }

    @Override
    public boolean checkUserNameUnique(SysUser sysUser) {
        // 判断是否自设ID 如果有则使用自设的  否则使用MySQL自增的[null或者<1都可以实现自增]
        long sysUserId = ObjUtil.isEmpty(sysUser.getUserId()) ? -1L : sysUser.getUserId();
        SysUser info = sysuserMapper.checkUserNameUnique(sysUser.getUserName());
        if (ObjUtil.isNotNull(info) && info.getUserId() != sysUserId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean registerUser(SysUser sysUser) {
        return sysuserMapper.insert(sysUser) > 0;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     */
    @Override
    public BaseUser selectSysUserByUserName(String username) {
        SysUser sysUser = sysuserMapper.selectSysUserByUserName(username);
        if (ObjUtil.isEmpty(sysUser)) {
            throw new RuntimeException("用户不存在");
        }
        return new BaseUser(sysUser.getUserId(), sysUser.getPassword(), sysUser.getPassword(), sysUser.getStatus());
    }

    @Override
    public Set<String> getMenuPermission(BaseUser user) {
        // TODO 2024/4/4/13:05 获取权限菜单列表
        HashSet<String> set = new HashSet<>();
        set.add(Constants.ALL_PERMISSION);
        return set;
    }
}
