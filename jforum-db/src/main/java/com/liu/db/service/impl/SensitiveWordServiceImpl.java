package com.liu.db.service.impl;

import com.liu.db.entity.SensitiveWord;
import com.liu.db.mapper.SensitiveWordMapper;
import com.liu.db.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 敏感词实际业务层 sensitive_word
 *
 * @author JIE
 * @since 2024-05-16
 */
@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitivewordMapper;

    @Override
    public List<SensitiveWord> selectSensitiveWordList(SensitiveWord sensitiveword) {
        return sensitivewordMapper.selectSensitiveWordList(sensitiveword);
    }

    @Override
    public SensitiveWord selectSensitiveWordById(Long id) {
        return sensitivewordMapper.selectSensitiveWordById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SensitiveWord sensitiveword) {
        return sensitivewordMapper.insert(sensitiveword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SensitiveWord sensitiveword) {
        return sensitivewordMapper.update(sensitiveword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] ids) {
        return sensitivewordMapper.deleteById(ids);
    }
}
