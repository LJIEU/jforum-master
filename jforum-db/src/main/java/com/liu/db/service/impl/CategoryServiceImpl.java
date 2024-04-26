package com.liu.db.service.impl;

import com.liu.db.entity.Category;
import com.liu.db.mapper.CategoryMapper;
import com.liu.db.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子分类实际业务层 category
 *
 * @author JIE
 * @since 2024-04-23
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectCategoryList(Category category) {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public Category selectCategoryByCategoryId(Long categoryId) {
        return categoryMapper.selectCategoryByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] categoryIds) {
        return categoryMapper.deleteById(categoryIds);
    }

    @Override
    public List<Long> getChildIdByPid(Long pid) {
        return categoryMapper.getChildIdByPid(pid);
    }
}
