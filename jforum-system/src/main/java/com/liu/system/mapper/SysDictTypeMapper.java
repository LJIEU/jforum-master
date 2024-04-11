package com.liu.system.mapper;

import com.liu.system.dao.SysDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典类型接口层 sys_dict_type
 *
 * @author JIE
 * @since 2024-04-11
 */
@Mapper
public interface SysDictTypeMapper {


    /**
     * 查询 字典类型 列表
     *
     * @param sysdicttype 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysDictType> selectSysDictTypeList(SysDictType sysdicttype);

    /**
     * 获取 字典类型 详细信息
     *
     * @param dictId 字典类型ID
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
     * 批量删除 字典类型
     *
     * @param dictIds 字典ID列表
     * @return 删除情况
     */
    int deleteById(@Param("dictIds") Long[] dictIds);
}
