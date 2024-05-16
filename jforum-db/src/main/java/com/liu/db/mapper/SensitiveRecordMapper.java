package com.liu.db.mapper;

import com.liu.db.entity.SensitiveRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 屏蔽记录接口层 sensitive_record
 *
 * @author JIE
 * @since 2024-05-16
 */
@Mapper
public interface SensitiveRecordMapper {


    /**
     * 查询 屏蔽记录 列表
     *
     * @param sensitiverecord 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SensitiveRecord> selectSensitiveRecordList(SensitiveRecord sensitiverecord);

    /**
     * 获取 屏蔽记录 详细信息
     *
     * @param id 屏蔽记录ID
     * @return 返回屏蔽记录信息
     */
    SensitiveRecord selectSensitiveRecordById(Long id);

    /**
     * 新增 屏蔽记录
     *
     * @param sensitiverecord 屏蔽记录
     * @return 添加情况
     */
    int insert(SensitiveRecord sensitiverecord);

    /**
     * 修改 屏蔽记录
     *
     * @param sensitiverecord 屏蔽记录
     * @return 修改情况
     */
    int update(SensitiveRecord sensitiverecord);

    /**
     * 批量删除 屏蔽记录
     *
     * @param ids 列表
     * @return 删除情况
     */
    int deleteById(@Param("ids") Long[] ids);
}
