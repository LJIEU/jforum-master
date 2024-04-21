package com.liu.db.service;

import com.liu.db.entity.SysDept;

import java.util.List;

/**
 * 部门服务层 sys_dept
 *
 * @author JIE
 * @since 2024-04-11
 */
public interface SysDeptService {
    /**
     * 查询 部门 列表
     *
     * @param sysdept 部门
     * @return 返回 分页结果
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
     * 删除 部门
     *
     * @param deptIds 部门ID 列表
     * @return 删除情况
     */
    int delete(Long[] deptIds);

    /**
     * 根据 部门名称 查询 部门ID
     *
     * @param deptName 部门名称
     * @return 返回 部门ID
     */
    Long selectSysDeptByDeptName(String deptName);
}
