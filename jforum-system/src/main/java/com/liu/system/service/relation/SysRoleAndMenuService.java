package com.liu.system.service.relation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.liu.core.constant.enume.ToolEnum;
import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.ToolUtils;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.mapper.relation.SysRoleAndMenuMapper;
import com.liu.system.service.SysRoleService;
import com.liu.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 角色和菜单关联业务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/05 13:30
 */
@Slf4j
@Service
public class SysRoleAndMenuService {
    @Autowired
    private SysRoleAndMenuMapper roleAndMenuMapper;

    public List<SysMenu> selectMenuListByRoleId(Long roleId) {
        return roleAndMenuMapper.selectMenuListByRoleId(roleId);
    }

    public List<SysRole> selectRoleByMenuId(Long menuId) {
        return roleAndMenuMapper.selectRoleByMenuId(menuId);
    }

    /**
     * 分配菜单 包括按钮权限
     *
     * @param roleId   角色ID
     * @param menusIds 菜单列表
     * @param username 当前赋权的用户 ==》 用户名
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, Long[] menusIds, String username) {
        // 查询 当前赋值的用户权限
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(username);
        if (user == null) {
            throw new UserNotExistsException();
        }
        // 查询该用户的权限列表
        List<SysRole> roleList = SpringUtils.getBean(SysUserAndRoleService.class).getRoleByUserId(user.getUserId());
        List<Long> currUserMenuId = new ArrayList<>();
        roleList.forEach(v -> {
            List<SysMenu> menus = roleAndMenuMapper.selectMenuListByRoleId(v.getRoleId());
            for (SysMenu menu : menus) {
                currUserMenuId.add(menu.getMenuId());
            }
        });
        log.info("当前用户拥有的菜单列表:" + currUserMenuId);
        // 查询 roleId 是否存在
        SysRole sysRole = SpringUtils.getBean(SysRoleService.class).selectSysRoleByRoleId(roleId);
        if (ObjUtil.isEmpty(sysRole)) {
            throw new RuntimeException("角色不存在");
        }
        /**
         * 分析: 情况一:
         * 当前用户所拥有的权限菜单 [1,2,3,4]
         * 需要给角色赋权的菜单ID  [1,2,5]   ====》 显然 5 是赋权不了的 越权操作了
         * 当前角色所拥有的菜单ID  []
         * 根据这三条信息得出：>   [1,2] ==> 添加操作
         * 分析: 情况二:
         * 当前用户所拥有的权限菜单 [1,2,3,4]
         * 需要给角色赋权的菜单ID  [2,3]
         * 当前角色所拥有的菜单ID  [1,2]
         * 根据这三条信息得出：>   [2,3] ==> 修改操作
         * 分析: 情况三:
         * 当前用户所拥有的权限菜单 [1,2,3,4]
         * 需要给角色赋权的菜单ID  [1]
         * 当前角色所拥有的菜单ID  [1,2]
         * 根据这三条信息得出：>   [1]   ==> 删除操作  依次类推 还有添加+修改  修改+删除
         */
        // 1.从 赋权者 中 筛选出 可添加的权限 以赋权者为准 就是两个集合的交集
        List<Long> requiredMenus = Arrays.stream(menusIds).toList();
        requiredMenus = CollUtil.intersection(currUserMenuId, requiredMenus).stream().toList();
        if (requiredMenus == null || CollUtil.isEmpty(requiredMenus)) {
            // 如果是空的话  那就删除 只能删除当前用户所拥有的权限
            currUserMenuId.forEach(v -> {
                delete(v, roleId);
            });
        }
        // 查询当前角色已经拥有的MenusId
        List<Long> dbRoleMenus = selectMenuListByRoleId(roleId).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        Map<ToolEnum, Object> multiple = ToolUtils.multiple(dbRoleMenus, requiredMenus);
        Map<Long, Long> update = (Map<Long, Long>) multiple.get(ToolEnum.UPDATE);
        if (!MapUtil.isEmpty(update)) {
            // 更新操作
            update.forEach((db, n) -> {
                update(db, n, roleId);
            });
        }
        List<Long> add = (List<Long>) multiple.get(ToolEnum.ADD);
        add.forEach(v -> {
            insert(v, roleId);
        });
        List<Long> delete = (List<Long>) multiple.get(ToolEnum.DELETE);
        delete.forEach(v -> {
            delete(v, roleId);
        });
    }

    public void insert(Long newMenuId, Long roleId) {
        roleAndMenuMapper.insert(roleId, newMenuId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long oldMenuId, Long newMenuId, Long roleId) {
        roleAndMenuMapper.update(roleId, oldMenuId, newMenuId);
    }

    public void delete(Long oldMenuId, Long roleId) {
        roleAndMenuMapper.delete(roleId, oldMenuId);
    }

}
