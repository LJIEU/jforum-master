package com.liu.db.converter;

import com.liu.core.converter.TreeConverter;
import com.liu.db.entity.SysMenu;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/03 16:04
 */
public class SysMenuConverter implements TreeConverter<SysMenu> {
    @Override
    public List<SysMenu> getChildren(SysMenu data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(SysMenu data, List<SysMenu> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(SysMenu data) {
        return data.getParentId();
    }

    @Override
    public Long getId(SysMenu data) {
        return data.getMenuId();
    }
}
