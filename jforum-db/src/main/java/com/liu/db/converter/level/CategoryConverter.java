package com.liu.db.converter.level;

import com.liu.core.converter.LevelConverter;
import com.liu.db.entity.Category;
import com.liu.db.vo.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/24 15:04
 */
public class CategoryConverter implements LevelConverter<Category, Level> {
    @Override
    public Long getPid(Category v) {
        return v.getPid();
    }

    @Override
    public Level dTtoL(Category category) {
        Level level = new Level();
        level.setValue(category.getCategoryId());
        level.setLabel(category.getName());
        level.setChildren(new ArrayList<>());
        return level;
    }

    @Override
    public Long getId(Category category) {
        return category.getCategoryId();
    }

    @Override
    public List<Level> getChildrenByL(Level level) {
        if (level.getChildren() == null) {
            level.setChildren(new ArrayList<>());
        }
        return level.getChildren();
    }
}
