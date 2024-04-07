package com.liu.system.service.relation;

import cn.hutool.core.util.ObjUtil;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.mapper.relation.SysRoleAndMenuMapper;
import com.liu.system.service.SysRoleService;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 角色和菜单关联业务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 13:30
 */
@Service
public class SysRoleAndMenuService {
    @Autowired
    private SysRoleAndMenuMapper roleAndMenuMapper;

    public List<SysMenu> selectMenuListByRoleId(Long roleId) {
        return roleAndMenuMapper.selectMenuListByRoleId(roleId);
    }

    public List<SysRole> selectRoleByMenuId(Long menuId) {
        return roleAndMenuMapper.selectRoleByMenuId(menuId);
    }

    /**
     * 分配菜单 包括按钮权限
     *
     * @param roleId   角色ID
     * @param menusIds 菜单列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, Long[] menusIds) {
        // 查询 roleId 是否存在
        SysRole sysRole = SpringUtils.getBean(SysRoleService.class).selectSysRoleByRoleId(roleId);
        if (ObjUtil.isEmpty(sysRole)) {
            throw new RuntimeException("角色不存在");
        }
        // 查询当前角色已经拥有的MenusId
        List<Long> menuIdList = selectMenuListByRoleId(roleId).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        List<Long> toBeAddMenus = List.of(menusIds);
        // 相减  ==> 待添加[1,2,3] 减去 已拥有的[1,4,3]  ==> 未添加的[2]
        List<Long> subtract = ListUtils.subtract(toBeAddMenus, menuIdList);
        for (Long menuId : subtract) {
            roleAndMenuMapper.insert(roleId, menuId);
        }
    }
}
