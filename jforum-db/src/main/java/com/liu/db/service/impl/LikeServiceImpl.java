package com.liu.db.service.impl;

import com.liu.db.entity.Like;
import com.liu.db.mapper.LikeMapper;
import com.liu.db.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 点赞实际业务层 like
 *
 * @author JIE
 * @since 2024-05-11
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public List<Like> selectLikeList(Like like) {
        return likeMapper.selectLikeList(like);
    }

    @Override
    public Like selectLikeByLikeId(Long likeId) {
        return likeMapper.selectLikeByLikeId(likeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Like like) {
        return likeMapper.insert(like);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Like like) {
        return likeMapper.update(like);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] likeIds) {
        return likeMapper.deleteById(likeIds);
    }
}
