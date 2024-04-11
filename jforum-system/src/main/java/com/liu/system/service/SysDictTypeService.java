package com.liu.system.service;

import com.liu.system.dao.SysDictType;

import java.util.List;

/**
 * 字典类型服务层 sys_dict_type
 *
 * @author JIE
 * @since 2024-04-11
 */
public interface SysDictTypeService {
    /**
     * 查询 字典类型 列表
     *
     * @param sysdicttype 字典类型
     * @return 返回 分页结果
     */
    List<SysDictType> selectSysDictTypeList(SysDictType sysdicttype);


    /**
     * 获取 字典类型 详细信息
     *
     * @param dictId 字典ID
     * @return 返回字典类型信息
     */
        SysDictType selectSysDictTypeByDictId(Long dictId);

    /**
     * 新增 字典类型
     *
     * @param sysdicttype 字典类型
     * @return 添加情况
     */
    int insert(SysDictType sysdicttype);

    /**
     * 修改 字典类型
     *
     * @param sysdicttype 字典类型
     * @return 修改情况
     */
    int update(SysDictType sysdicttype);

    /**
     * 删除 字典类型
     *
     * @param dictIds 字典ID 列表
     * @return 删除情况
     */
    int delete(Long[] dictIds);
}
