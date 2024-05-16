package com.liu.db.mapper;

import com.liu.db.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 敏感词接口层 sensitive_word
 *
 * @author JIE
 * @since 2024-05-16
 */
@Mapper
public interface SensitiveWordMapper {


    /**
     * 查询 敏感词 列表
     *
     * @param sensitiveword 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SensitiveWord> selectSensitiveWordList(SensitiveWord sensitiveword);

    /**
     * 获取 敏感词 详细信息
     *
     * @param id 敏感词ID
     * @return 返回敏感词信息
     */
    SensitiveWord selectSensitiveWordById(Long id);

    /**
     * 新增 敏感词
     *
     * @param sensitiveword 敏感词
     * @return 添加情况
     */
    int insert(SensitiveWord sensitiveword);

    /**
     * 修改 敏感词
     *
     * @param sensitiveword 敏感词
     * @return 修改情况
     */
    int update(SensitiveWord sensitiveword);

    /**
     * 批量删除 敏感词
     *
     * @param ids 列表
     * @return 删除情况
     */
    int deleteById(@Param("ids") Long[] ids);
}
