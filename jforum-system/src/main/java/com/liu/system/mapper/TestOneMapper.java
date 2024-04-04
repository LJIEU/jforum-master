package com.liu.system.mapper;

import com.liu.system.dao.TestOne;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试接口层 test_one
 *
 * @author JIE
 * @since 2024-03-31
 */
@Mapper
public interface TestOneMapper {


    /**
     * 查询 测试 列表
     *
     * @param testone 可以根据字段查询
     * @return 返回 列表集合
     */
    List<TestOne> selectTestOneList(TestOne testone);

    /**
     * 获取 测试 详细信息
     *
     * @param oneId 测试ID
     * @return 返回测试信息
     */
    TestOne selectTestOneByOneId(Long oneId);

    /**
     * 新增 测试
     *
     * @param testone 测试
     * @return 添加情况
     */
    int insert(TestOne testone);

    /**
     * 修改 测试
     *
     * @param testone 测试
     * @return 修改情况
     */
    int update(TestOne testone);

    /**
     * 批量删除 测试
     *
     * @param oneIds ID列表
     * @return 删除情况
     */
    int deleteById(@Param("oneId") Long oneId);
}
