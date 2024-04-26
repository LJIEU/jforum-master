package com.liu.db.service.impl;

import com.liu.db.entity.Post;
import com.liu.db.mapper.PostMapper;
import com.liu.db.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子实际业务层 post
 *
 * @author JIE
 * @since 2024-04-23
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> selectPostList(Post post) {
        return postMapper.selectPostList(post);
    }

    @Override
    public Post selectPostByPostId(String postId) {
        return postMapper.selectPostByPostId(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Post post) {
        return postMapper.insert(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Post post) {
        return postMapper.update(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String[] postIds) {
        return postMapper.deleteById(postIds);
    }
}
