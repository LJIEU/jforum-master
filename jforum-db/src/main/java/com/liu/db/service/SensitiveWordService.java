package com.liu.db.service;


import com.liu.db.entity.SensitiveWord;

import java.util.List;

/**
 * 敏感词服务层 sensitive_word
 *
 * @author JIE
 * @since 2024-05-16
 */
public interface SensitiveWordService {
    /**
     * 查询 敏感词 列表
     *
     * @param sensitiveword 敏感词
     * @return 返回 分页结果
     */
    List<SensitiveWord> selectSensitiveWordList(SensitiveWord sensitiveword);


    /**
     * 获取 敏感词 详细信息
     *
     * @param id
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
     * 删除 敏感词
     *
     * @param ids 列表
     * @return 删除情况
     */
    int delete(Long[] ids);
}
