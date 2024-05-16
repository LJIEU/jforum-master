package com.liu.db.mapper.relation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 16:50
 */
@Mapper
public interface PostAndTagMapper {

    /**
     * 插入数据
     *
     * @param tagId  标签ID
     * @param postId 帖子ID
     */
    void insert(@Param("tagId") String tagId, @Param("postId") String postId);


    /**
     * 查询 帖子绑定标签
     * @param postId 帖子ID
     * @return 返回结果
     */
    List<String> selectTagsByPostId(@Param("postId") String postId);

    /**
     * 删除
     * @param postId 帖子ID
     */
    void delete(String postId);
}
