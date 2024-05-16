package com.liu.db.service.relation;

import com.liu.db.mapper.relation.PostAndTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 16:47
 */
@Service
public class PostAndTagService {
    @Autowired
    private PostAndTagMapper postAndTagMapper;


    @Transactional(rollbackFor = Exception.class)
    public void insert(String tagId, String postId) {
        postAndTagMapper.insert(tagId, postId);
    }

    public List<String> selectTagsByPostId(String postId) {
        return postAndTagMapper.selectTagsByPostId(postId);
    }

    public void delete(String postId) {
        postAndTagMapper.delete(postId);
    }
}
