package com.liu.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.system.dao.SysMenu;
import com.liu.system.mapper.SysMenuMapper;
import com.liu.system.service.SysMenuService;
import com.liu.system.service.SysRoleService;
import com.liu.system.service.relation.SysRoleAndMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单权限实际业务层 sys_menu
 *
 * @author JIE
 * @since 2024-04-03
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysmenuMapper;

    @Override
    public List<SysMenu> selectSysMenuList(SysMenu sysmenu) {
        return sysmenuMapper.selectSysMenuList(sysmenu);
    }

    @Override
    public SysMenu selectSysMenuByMenuId(Long menuId) {
        return sysmenuMapper.selectSysMenuByMenuId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysMenu sysMenu) {
        sysmenuMapper.insert(sysMenu);
        // 获取菜单ID
        SysMenu menu = sysmenuMapper.getItem(sysMenu.getMenuName());
        if (menu == null) {
            throw new RuntimeException("未找到菜单~");
        }
        // 并且给当前登录用户的角色 赋予菜单权限
        SysRoleAndMenuService roleAndMenuService = SpringUtils.getBean(SysRoleAndMenuService.class);
        String currRoleName = SecurityUtils.currRoleName();
        Long roleId = SpringUtils.getBean(SysRoleService.class).getItem(currRoleName);
        if (roleId == null) {
            throw new RuntimeException("角色不存在!");
        }
        // 2024/4/16/15:17 查询当前用户的角色ID 然后将该菜单 与该角色ID 关联起来
        roleAndMenuService.insert(menu.getMenuId(), roleId);
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysMenu sysmenu) {
        return sysmenuMapper.update(sysmenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] menuIds) {
        int count = 0;
        for (Long menuId : menuIds) {
            sysmenuMapper.deleteById(menuId);
            count++;
        }
        return count;
    }

    @Override
    public List<SysMenu> selectSysMenuListByStatusOrKeywords(String status, String keywords) {
        SysMenu sysMenu = new SysMenu();
        if ("1".equals(status) || "0".equals(status)) {
            sysMenu.setStatus(status);
        }
        if (StrUtil.isNotEmpty(keywords) && !"undefined".equals(keywords)) {
            sysMenu.setMenuName(keywords);
        }
        return sysmenuMapper.selectSysMenuList(sysMenu);
    }

    @Override
    public SysMenu getItem(String menuName) {
        return sysmenuMapper.getItem(menuName);
    }
}
