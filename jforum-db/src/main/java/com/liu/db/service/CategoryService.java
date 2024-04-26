package com.liu.db.service;

import com.liu.db.entity.Category;

import java.util.List;

/**
 * 帖子分类服务层 category
 *
 * @author JIE
 * @since 2024-04-23
 */
public interface CategoryService {
    /**
     * 查询 帖子分类 列表
     *
     * @param category 帖子分类
     * @return 返回 分页结果
     */
    List<Category> selectCategoryList(Category category);


    /**
     * 获取 帖子分类 详细信息
     *
     * @param categoryId 分类ID
     * @return 返回帖子分类信息
     */
    Category selectCategoryByCategoryId(Long categoryId);

    /**
     * 新增 帖子分类
     *
     * @param category 帖子分类
     * @return 添加情况
     */
    int insert(Category category);

    /**
     * 修改 帖子分类
     *
     * @param category 帖子分类
     * @return 修改情况
     */
    int update(Category category);

    /**
     * 删除 帖子分类
     *
     * @param categoryIds 分类ID 列表
     * @return 删除情况
     */
    int delete(Long[] categoryIds);


    /**
     * 获取子列表ID
     * @param pid 父ID
     * @return ID集合
     */
    List<Long> getChildIdByPid(Long pid);
}
