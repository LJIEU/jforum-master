package com.liu.db.mapper;

import com.liu.core.config.dynamic.DataSource;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.db.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子分类接口层 category
 *
 * @author JIE
 * @since 2024-04-23
 */
@DataSource(DataSourceType.MASTER)
@Mapper
public interface CategoryMapper {


    /**
     * 查询 帖子分类 列表
     *
     * @param category 可以根据字段查询
     * @return 返回 列表集合
     */
    List<Category> selectCategoryList(Category category);

    /**
     * 获取 帖子分类 详细信息
     *
     * @param categoryId 帖子分类ID
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
     * 批量删除 帖子分类
     *
     * @param categoryIds 分类ID列表
     * @return 删除情况
     */
    int deleteById(@Param("categoryIds") Long[] categoryIds);

    /**
     * 获取子列表ID
     *
     * @param pid 父ID
     * @return ID集合
     */
    List<Long> getChildIdByPid(Long pid);
}
