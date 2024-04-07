package com.liu.system.mapper.relation;

import com.liu.system.dao.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 11:45
 */
@Mapper
public interface SysUserAndRoleMapper {
    List<SysRole> getRoleByUserId(Long userId);
}
