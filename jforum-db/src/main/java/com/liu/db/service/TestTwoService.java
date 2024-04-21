package com.liu.db.service;

import com.liu.db.entity.TestTwo;

import java.util.List;

/**
 * 测试服务层 test_two
 *
 * @author JIE
 * @since 2024-04-02
 */
public interface TestTwoService {
    /**
     * 查询 测试 列表
     *
     * @param testtwo 测试
     * @return 返回 分页结果
     */
    List<TestTwo> selectTestTwoList(TestTwo testtwo);


    /**
     * 获取 测试 详细信息
     *
     * @param twoId ID
     * @return 返回测试信息
     */
    TestTwo selectTestTwoByTwoId(Long twoId);

    /**
     * 新增 测试
     *
     * @param testtwo 测试
     * @return 添加情况
     */
    int insert(TestTwo testtwo);

    /**
     * 修改 测试
     *
     * @param testtwo 测试
     * @return 修改情况
     */
    int update(TestTwo testtwo);

    /**
     * 删除 测试
     *
     * @param twoIds ID 列表
     * @return 删除情况
     */
    int delete(Long[] twoIds);
}
