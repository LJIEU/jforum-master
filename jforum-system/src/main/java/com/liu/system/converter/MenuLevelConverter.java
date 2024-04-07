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
    public List<SysMenu> getChildrenByD(SysMenu menu) {
        return menu.getChildren();
    }

    @Override
    public List<MenuLevel> getChildrenByL(MenuLevel menuLevel) {
        return menuLevel.getChildren();
    }

    @Override
    public void setChildren(SysMenu menu, List<SysMenu> children) {
        menu.setChildren(children);
    }

    @Override
    public void setChildrenByL(MenuLevel level, List<MenuLevel> children) {
        level.setChildren(children);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
