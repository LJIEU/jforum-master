package com.liu.system.converter.level;

import com.liu.core.converter.LevelConverter;
import com.liu.system.dao.SysDept;
import com.liu.system.vo.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/11 17:33
 */
public class DeptLevelConverter implements LevelConverter<SysDept, Level> {

    @Override
    public Long getPid(SysDept v) {
        return v.getParentId();
    }

    @Override
    public Level dTtoL(SysDept parent) {
        Level level = new Level();
        level.setValue(parent.getDeptId());
        level.setLabel(parent.getDeptName());
        level.setChildren(new ArrayList<>());
        return level;
    }

    @Override
    public Long getId(SysDept parent) {
        return parent.getDeptId();
    }

    @Override
    public List<Level> getChildrenByL(Level level) {
        if (level.getChildren() == null) {
            level.setChildren(new ArrayList<>());
        }
        return level.getChildren();
    }
}
