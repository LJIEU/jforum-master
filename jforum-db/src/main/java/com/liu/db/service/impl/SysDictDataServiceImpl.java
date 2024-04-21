package com.liu.db.service.impl;

import com.liu.db.entity.SysDictData;
import com.liu.db.mapper.SysDictDataMapper;
import com.liu.db.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据实际业务层 sys_dict_data
 *
 * @author JIE
 * @since 2024-04-11
 */
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    @Autowired
    private SysDictDataMapper sysdictdataMapper;

    @Override
    public List<SysDictData> selectSysDictDataList(SysDictData sysdictdata) {
        return sysdictdataMapper.selectSysDictDataList(sysdictdata);
    }

    @Override
    public SysDictData selectSysDictDataByDictCode(Long dictCode) {
        return sysdictdataMapper.selectSysDictDataByDictCode(dictCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysDictData sysdictdata) {
        return sysdictdataMapper.insert(sysdictdata);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysDictData sysdictdata) {
        return sysdictdataMapper.update(sysdictdata);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] dictCodes) {
        int count = 0;
        for (Long dictCode : dictCodes) {
            sysdictdataMapper.deleteById(dictCode);
            count++;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAllDictType(String oldDictType, String newDictType) {
        sysdictdataMapper.updateAllDictType(oldDictType, newDictType);
    }
}
