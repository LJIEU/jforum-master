package com.liu.db.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.liu.core.constant.Constants;
import com.liu.core.constant.UserConstants;
import com.liu.core.model.BaseUser;
import com.liu.db.entity.SysUser;
import com.liu.db.mapper.SysUserMapper;
import com.liu.db.service.SysUserService;
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
public class SysUserServiceImpl implements SysUserService {

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
        int count = 0;
        for (Long userId : userIds) {
            sysuserMapper.deleteById(userId);
            count++;
        }

        // TODO 2024/4/11/16:43 这里用户角色关联表也需要删除
        return count;
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

    @Override
    public SysUser getItemByUserName(String username) {
        return sysuserMapper.selectSysUserByUserName(username);
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


    @Override
    public List<String> selectAdminNickNameList() {
        return sysuserMapper.selectAdminNickNameList();
    }
}
