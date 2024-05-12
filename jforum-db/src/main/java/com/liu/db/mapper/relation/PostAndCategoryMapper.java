package com.liu.db.mapper.relation;

import com.liu.core.config.dynamic.DataSource;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/24 10:18
 */
@Mapper
@DataSource(value = DataSourceType.MASTER)
public interface PostAndCategoryMapper {
    /**
     * 根据 帖子ID 获取 分类
     *
     * @param postId 帖子ID
     * @return 分类信息
     */
    Category selectCategoryByPostId(String postId);

    /**
     * 根据 类别ID 获取 帖子列表
     *
     * @param categoryId 类别ID
     * @return 帖子集合
     */
    List<Post> selectPostByCategoryId(Long categoryId);

    /**
     * 插入
     *
     * @param categoryId 类别ID
     * @param postId     帖子ID
     */
    void insert(@Param("categoryId") Long categoryId, @Param("postId") String postId);
}
