package com.liu.system.converter;

import com.liu.core.converter.LevelConverter;
import com.liu.system.dao.SysMenu;
import com.liu.system.vo.level.MenuLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/07 14:53
 */
public class MenuLevelConverter implements LevelConverter<SysMenu, MenuLevel> {
    @Override
    public Long getPid(SysMenu v) {
        return v.getParentId();
    }

    @Override
    public MenuLevel dTtoL(SysMenu menu) {
        MenuLevel level = new MenuLevel();
        level.setValue(menu.getMenuId());
        level.setLabel(menu.getMenuName());
        level.setChildren(new ArrayList<>());
        return level;
    }

    @Override
    public Long getId(SysMenu menu) {
        return menu.getMenuId();
    }

    @Override
    public List<MenuLevel> getChildrenByL(MenuLevel menuLevel) {
        if (menuLevel.getChildren() == null) {
            menuLevel.setChildren(new ArrayList<>());
        }
        return menuLevel.getChildren();
    }
}
