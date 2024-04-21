package com.liu.db.service.impl;

import com.liu.db.entity.SysDictType;
import com.liu.db.mapper.SysDictTypeMapper;
import com.liu.db.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典类型实际业务层 sys_dict_type
 *
 * @author JIE
 * @since 2024-04-11
 */
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysdicttypeMapper;

    @Override
    public List<SysDictType> selectSysDictTypeList(SysDictType sysdicttype) {
        return sysdicttypeMapper.selectSysDictTypeList(sysdicttype);
    }

    @Override
    public SysDictType selectSysDictTypeByDictId(Long dictId) {
        return sysdicttypeMapper.selectSysDictTypeByDictId(dictId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysDictType sysdicttype) {
        return sysdicttypeMapper.insert(sysdicttype);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysDictType sysdicttype) {
        return sysdicttypeMapper.update(sysdicttype);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] dictIds) {
        int count = 0;
        for (Long dictId : dictIds) {
            sysdicttypeMapper.deleteById(dictId);
            count++;
        }
        return count;
    }
}
