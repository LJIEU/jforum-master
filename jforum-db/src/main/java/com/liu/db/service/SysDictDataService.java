package com.liu.db.service;

import com.liu.db.entity.SysDictData;

import java.util.List;

/**
 * 字典数据服务层 sys_dict_data
 *
 * @author JIE
 * @since 2024-04-11
 */
public interface SysDictDataService {
    /**
     * 查询 字典数据 列表
     *
     * @param sysdictdata 字典数据
     * @return 返回 分页结果
     */
    List<SysDictData> selectSysDictDataList(SysDictData sysdictdata);


    /**
     * 获取 字典数据 详细信息
     *
     * @param dictCode 字典编码
     * @return 返回字典数据信息
     */
        SysDictData selectSysDictDataByDictCode(Long dictCode);

    /**
     * 新增 字典数据
     *
     * @param sysdictdata 字典数据
     * @return 添加情况
     */
    int insert(SysDictData sysdictdata);

    /**
     * 修改 字典数据
     *
     * @param sysdictdata 字典数据
     * @return 修改情况
     */
    int update(SysDictData sysdictdata);

    /**
     * 删除 字典数据
     *
     * @param dictCodes 字典编码 列表
     * @return 删除情况
     */
    int delete(Long[] dictCodes);

    /**
     * 更新所有 dict_type
     * @param oldDictType 旧的字典类型
     * @param newDictType 新的
     */
    void updateAllDictType(String oldDictType, String newDictType);
}
