package com.liu.db.mapper.relation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
