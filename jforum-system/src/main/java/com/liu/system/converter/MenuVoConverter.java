package com.liu.system.converter;

import com.liu.core.converter.TreeConverter;
import com.liu.system.vo.MenuVo;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/10 16:56
 */
public class MenuVoConverter implements TreeConverter<MenuVo> {
    @Override
    public List<MenuVo> getChildren(MenuVo data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(MenuVo data, List<MenuVo> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(MenuVo data) {
        return data.getParentId();
    }

    @Override
    public Long getId(MenuVo data) {
        return data.getId();
    }
}
