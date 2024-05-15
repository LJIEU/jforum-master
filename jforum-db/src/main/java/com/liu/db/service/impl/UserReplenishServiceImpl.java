package com.liu.db.service.impl;

import com.liu.db.entity.UserReplenish;
import com.liu.db.mapper.UserReplenishMapper;
import com.liu.db.service.UserReplenishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户补充信息实际业务层 user_replenish
 *
 * @author JIE
 * @since 2024-05-15
 */
@Service
public class UserReplenishServiceImpl implements UserReplenishService {

    @Autowired
    private UserReplenishMapper userreplenishMapper;

    @Override
    public List<UserReplenish> selectUserReplenishList(UserReplenish userreplenish) {
        return userreplenishMapper.selectUserReplenishList(userreplenish);
    }

    @Override
    public UserReplenish selectUserReplenishByUserId(Long userId) {
        return userreplenishMapper.selectUserReplenishByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(UserReplenish userreplenish) {
        return userreplenishMapper.insert(userreplenish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(UserReplenish userreplenish) {
        return userreplenishMapper.update(userreplenish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] userIds) {
        return userreplenishMapper.deleteById(userIds);
    }
}
