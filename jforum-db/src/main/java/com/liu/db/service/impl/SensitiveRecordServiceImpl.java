package com.liu.db.service.impl;

import com.liu.db.entity.SensitiveRecord;
import com.liu.db.mapper.SensitiveRecordMapper;
import com.liu.db.service.SensitiveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 屏蔽记录实际业务层 sensitive_record
 *
 * @author JIE
 * @since 2024-05-16
 */
@Service
public class SensitiveRecordServiceImpl implements SensitiveRecordService {

    @Autowired
    private SensitiveRecordMapper sensitiverecordMapper;

    @Override
    public List<SensitiveRecord> selectSensitiveRecordList(SensitiveRecord sensitiverecord) {
        return sensitiverecordMapper.selectSensitiveRecordList(sensitiverecord);
    }

    @Override
    public SensitiveRecord selectSensitiveRecordById(Long id) {
        return sensitiverecordMapper.selectSensitiveRecordById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SensitiveRecord sensitiverecord) {
        return sensitiverecordMapper.insert(sensitiverecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SensitiveRecord sensitiverecord) {
        return sensitiverecordMapper.update(sensitiverecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] ids) {
        return sensitiverecordMapper.deleteById(ids);
    }
}
