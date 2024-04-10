package com.liu.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.liu.system.dao.SysMenu;
import com.liu.system.mapper.SysMenuMapper;
import com.liu.system.service.SysMenuService;
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
    public int insert(SysMenu sysmenu) {
        return sysmenuMapper.insert(sysmenu);
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
        if (StrUtil.isNotEmpty(keywords)&& !"undefined".equals(keywords)) {
            sysMenu.setMenuName(keywords);
        }
        return sysmenuMapper.selectSysMenuList(sysMenu);
    }
}
