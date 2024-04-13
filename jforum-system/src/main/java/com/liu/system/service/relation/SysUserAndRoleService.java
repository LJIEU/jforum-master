package com.liu.system.service.relation;

import cn.hutool.core.collection.CollUtil;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysRole;
import com.liu.system.mapper.relation.SysUserAndRoleMapper;
import com.liu.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 更新 关联数据
     *
     * @param userId  用户ID
     * @param roleIds 需要更新的角色列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Long userId, List<Long> roleIds) {
        // 获取用户 原有的角色列表
        List<Long> dbRoleIds = getRoleByUserId(userId).stream().map(SysRole::getRoleId).collect(Collectors.toList());
        // 取补集 不行 【要以 更新角色列表为准】
//        CollUtil.disjunction()
        // 1.更新列表中有 而 数据库中没有的数据 [1,2,3,4] [2,3,4,5] => [1]  这就是需要插入的数据
        // [1,2,3] [2,3,4,5] ==> [1]
        // [1,2,3]   [4,5]     ==> [1,2,3]
        List<Long> subtractToList = CollUtil.subtractToList(roleIds, dbRoleIds);
        // [2,3,4,5] [1,2,3,4] => [5] ==> 这个就是 需要改变的数据
        // [2,3,4,5] [1,2,3] ==>  [4,5]
        // [4,5]     [1,2]   ==>  [4,5]
        List<Long> updateTemp = CollUtil.subtractToList(dbRoleIds, roleIds);

        // 如果 subtractToList > updateTime 那就是 修改+添加
        if (subtractToList.size() > updateTemp.size()) {
            for (int i = 0; i < subtractToList.size(); i++) {
                if (i > updateTemp.size() - 1) {
                    // 进行添加操作
                    userAndRoleMapper.add(subtractToList.get(i), userId);
                } else {
                    // 更新
                    userAndRoleMapper.update(updateTemp.get(i), subtractToList.get(i), userId);
                }
            }
        } else if (subtractToList.size() < updateTemp.size()) {
            // updateTemp > subtractToList 那就是 修改+删除
            for (int i = 0; i < updateTemp.size(); i++) {
                if (i > subtractToList.size() - 1) {
                    // 进行删除操作
                    userAndRoleMapper.delete(updateTemp.get(i), userId);
                } else {
                    // 更新
                    userAndRoleMapper.update(updateTemp.get(i), subtractToList.get(i), userId);
                }
            }
        } else {
            // 否则就是更新数据即可
            for (int i = 0; i < updateTemp.size(); i++) {
                userAndRoleMapper.update(updateTemp.get(i), subtractToList.get(i), userId);
            }
        }
    }

    /**
     * 添加 角色关联列表
     *
     * @param userId  用户ID
     * @param roleIds 角色ID
     */
    public void add(Long userId, List<Long> roleIds) {
        // 角色ID必须是存在的才能添加
        SysRoleService roleService = SpringUtils.getBean(SysRoleService.class);
        for (Long roleId : roleIds) {
            SysRole sysRole = roleService.selectSysRoleByRoleId(roleId);
            if (sysRole == null) {
            } else {
                userAndRoleMapper.add(roleId, userId);
            }
        }
    }
}
