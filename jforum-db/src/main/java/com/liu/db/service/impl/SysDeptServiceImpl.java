package com.liu.db.service.impl;

import com.liu.db.entity.SysDept;
import com.liu.db.mapper.SysDeptMapper;
import com.liu.db.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门实际业务层 sys_dept
 *
 * @author JIE
 * @since 2024-04-11
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysdeptMapper;

    @Override
    public List<SysDept> selectSysDeptList(SysDept sysdept) {
        return sysdeptMapper.selectSysDeptList(sysdept);
    }

    @Override
    public SysDept selectSysDeptByDeptId(Long deptId) {
        return sysdeptMapper.selectSysDeptByDeptId(deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysDept sysdept) {
        return sysdeptMapper.insert(sysdept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysDept sysdept) {
        return sysdeptMapper.update(sysdept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long[] deptIds) {
        return sysdeptMapper.deleteById(deptIds);
    }

    @Override
    public Long selectSysDeptByDeptName(String deptName) {
        return sysdeptMapper.selectSysDeptByDeptName(deptName);
    }
}
