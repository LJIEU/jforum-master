package com.liu.db.service.relation;

import cn.hutool.core.util.StrUtil;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import com.liu.db.mapper.relation.PostAndCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: 帖子和类别关联服务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/24 10:17
 */
@Service
public class PostAndCategoryService {
    @Autowired
    private PostAndCategoryMapper postAndCategoryMapper;

    public Category selectCategoryByPostId(String postId) {
        if (StrUtil.isNotEmpty(postId)) {
            return postAndCategoryMapper.selectCategoryByPostId(postId);
        }
        return new Category();
    }

    public List<Post> selectPostByCategoryId(Long categoryId) {
        return postAndCategoryMapper.selectPostByCategoryId(categoryId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Long categoryId, String postId) {
        postAndCategoryMapper.insert(categoryId, postId);
    }

    public void update(Long categoryId, String postId) {
        postAndCategoryMapper.update(categoryId, postId);
    }
}
