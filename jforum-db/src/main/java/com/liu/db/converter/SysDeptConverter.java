package com.liu.db.converter;

import com.liu.core.converter.TreeConverter;
import com.liu.db.entity.SysDept;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 17:15
 */
public class SysDeptConverter implements TreeConverter<SysDept> {
    @Override
    public List<SysDept> getChildren(SysDept data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(SysDept data, List<SysDept> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(SysDept data) {
        return data.getParentId();
    }

    @Override
    public Long getId(SysDept data) {
        return data.getDeptId();
    }
}
