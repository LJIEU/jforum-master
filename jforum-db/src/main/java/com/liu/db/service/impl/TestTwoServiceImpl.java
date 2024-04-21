package com.liu.db.service.impl;

import com.liu.db.entity.TestTwo;
import com.liu.db.mapper.TestTwoMapper;
import com.liu.db.service.TestTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试实际业务层 test_two
 *
 * @author JIE
 * @since 2024-04-02
 */
@Service
public class TestTwoServiceImpl implements TestTwoService {

    @Autowired
    private TestTwoMapper testtwoMapper;

    @Override
    public List<TestTwo> selectTestTwoList(TestTwo testtwo) {
        return testtwoMapper.selectTestTwoList(testtwo);
    }

    @Override
    public TestTwo selectTestTwoByTwoId(Long twoId) {
        return testtwoMapper.selectTestTwoByTwoId(twoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TestTwo testtwo) {
        return testtwoMapper.insert(testtwo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TestTwo testtwo) {
        return testtwoMapper.update(testtwo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] twoIds) {
        return testtwoMapper.deleteById(twoIds);
    }
}
