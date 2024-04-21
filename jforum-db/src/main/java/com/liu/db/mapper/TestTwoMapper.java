package com.liu.db.mapper;

import com.liu.db.entity.TestTwo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试接口层 test_two
 *
 * @author JIE
 * @since 2024-04-02
 */
@Mapper
public interface TestTwoMapper {


    /**
     * 查询 测试 列表
     *
     * @param testtwo 可以根据字段查询
     * @return 返回 列表集合
     */
    List<TestTwo> selectTestTwoList(TestTwo testtwo);

    /**
     * 获取 测试 详细信息
     *
     * @param twoId 测试ID
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
     * 批量删除 测试
     *
     * @param twoIds ID列表
     * @return 删除情况
     */
    int deleteById(@Param("twoIds") Long[] twoIds);
}
