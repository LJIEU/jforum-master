package com.liu.system.service;

import com.liu.system.dao.TestOne;

import java.util.List;

/**
 * 测试服务层 test_one
 *
 * @author JIE
 * @since 2024-03-31
 */
public interface TestOneService {
    /**
     * 查询 测试 列表
     *
     * @param testone 测试
     * @return 返回 分页结果
     */
    List<TestOne> selectTestOneList(TestOne testone);


    /**
     * 获取 测试 详细信息
     *
     * @param oneId ID
     * @return 返回测试信息
     */
    TestOne selectTestOneByOneId(Long oneId);

    /**
     * 新增 测试
     *
     * @param testone 测试
     * @return 添加情况
     */
    Integer insert(TestOne testone);

    /**
     * 修改 测试
     *
     * @param testone 测试
     * @return 修改情况
     */
    int update(TestOne testone);

    /**
     * 删除 测试
     *
     * @param oneIds ID 列表
     * @return 删除情况
     */
    int delete(Long[] oneIds);
}
