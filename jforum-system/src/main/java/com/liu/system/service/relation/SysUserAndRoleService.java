package com.liu.system.service.relation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.liu.core.constant.enume.ToolEnum;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.ToolUtils;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.mapper.relation.SysUserAndRoleMapper;
import com.liu.system.service.SysRoleService;
import com.liu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
     * @param updateUserId 需要更新的用户ID
     * @param requireIds   需要更新的角色列表
     * @param username     操作者用户名称
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Long updateUserId, List<Long> requireIds, String username) {
        // 查询 操作者 角色ID
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        List<Long> operateUserRoleId = userAndRoleMapper.getRoleByUserId(user.getUserId()).stream()
                .map(SysRole::getRoleId).collect(Collectors.toList());
        // 查询 需要更新者 角色ID
        List<Long> dbRoleIds = getRoleByUserId(updateUserId).stream().map(SysRole::getRoleId).collect(Collectors.toList());
        requireIds = CollUtil.intersection(operateUserRoleId, requireIds).stream().toList();
        if (requireIds.isEmpty() && CollUtil.isNotEmpty(dbRoleIds)) {
            // 删除角色  删除操作者范围内的角色
            for (Long roleId : operateUserRoleId) {
                userAndRoleMapper.delete(roleId, updateUserId);
            }
            return;
        }
        Map<ToolEnum, Object> multiple = ToolUtils.multiple(dbRoleIds, requireIds);
        Map<Long, Long> update = (Map<Long, Long>) multiple.get(ToolEnum.UPDATE);
        if (!MapUtil.isEmpty(update)) {
            // 更新操作
            update.forEach((db, n) -> {
                update(db, n, updateUserId);
            });
        }
        List<Long> add = (List<Long>) multiple.get(ToolEnum.ADD);
        add.forEach(v -> {
            userAndRoleMapper.add(v, updateUserId);
        });
        List<Long> delete = (List<Long>) multiple.get(ToolEnum.DELETE);
        delete.forEach(v -> {
            userAndRoleMapper.delete(v, updateUserId);
        });
    }

    public void update(Long dbRoleId, Long newRoleId, Long userId) {
        userAndRoleMapper.update(dbRoleId, newRoleId, userId);
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
