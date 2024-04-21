package com.liu.db.converter.tree;

import com.liu.core.converter.TreeConverter;
import com.liu.db.vo.RoutesVo;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/06 10:12
 */
public class RoutesVoConverter implements TreeConverter<RoutesVo> {
    @Override
    public List<RoutesVo> getChildren(RoutesVo data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(RoutesVo data, List<RoutesVo> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(RoutesVo data) {
        return data.getPid();
    }

    @Override
    public Long getId(RoutesVo data) {
        return data.getId();
    }
}
