package com.liu.system.service.impl;

import com.liu.system.dao.TestOne;
import com.liu.system.mapper.TestOneMapper;
import com.liu.system.service.TestOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试实际业务层 test_one
 *
 * @author JIE
 * @since 2024-03-31
 */
@Service
public class TestOneServiceImpl implements TestOneService {

    @Autowired
    private TestOneMapper testoneMapper;

    @Override
    public List<TestOne> selectTestOneList(TestOne testone) {
        return testoneMapper.selectTestOneList(testone);
    }

    @Override
    public TestOne selectTestOneByOneId(Long oneId) {
        return testoneMapper.selectTestOneByOneId(oneId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(TestOne testone) {
        return testoneMapper.insert(testone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TestOne testone) {
        return testoneMapper.update(testone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] oneIds) {
        int count = 0;
        for (Long oneId : oneIds) {
            count += testoneMapper.deleteById(oneId);
        }
        return count;
    }
}
