package com.liu.system.service.impl;

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
    public List<SysMenu> selectSysMenuList(SysMenu sysmenu)
    {
        return sysmenuMapper.selectSysMenuList(sysmenu);
    }

    @Override
    public SysMenu selectSysMenuByMenuId(Long menuId) {
        return sysmenuMapper.selectSysMenuByMenuId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysMenu sysmenu)
    {
        return sysmenuMapper.insert(sysmenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysMenu sysmenu)
    {
        return sysmenuMapper.update(sysmenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] menuIds) {
        return sysmenuMapper.deleteById(menuIds);
    }
}
