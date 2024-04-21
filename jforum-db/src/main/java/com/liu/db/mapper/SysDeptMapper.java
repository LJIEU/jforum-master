package com.liu.db.mapper;

import com.liu.db.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门接口层 sys_dept
 *
 * @author JIE
 * @since 2024-04-11
 */
@Mapper
public interface SysDeptMapper {


    /**
     * 查询 部门 列表
     *
     * @param sysdept 可以根据字段查询
     * @return 返回 列表集合
     */
    List<SysDept> selectSysDeptList(SysDept sysdept);

    /**
     * 获取 部门 详细信息
     *
     * @param deptId 部门ID
     * @return 返回部门信息
     */
    SysDept selectSysDeptByDeptId(Long deptId);

    /**
     * 新增 部门
     *
     * @param sysdept 部门
     * @return 添加情况
     */
    int insert(SysDept sysdept);

    /**
     * 修改 部门
     *
     * @param sysdept 部门
     * @return 修改情况
     */
    int update(SysDept sysdept);

    /**
     * 批量删除 部门
     *
     * @param deptIds 部门ID列表
     * @return 删除情况
     */
    int deleteById(@Param("deptIds") Long[] deptIds);


    /**
     * 根据 部门名称 查询 部门ID
     *
     * @param deptName 部门名称
     */
    Long selectSysDeptByDeptName(String deptName);
}
