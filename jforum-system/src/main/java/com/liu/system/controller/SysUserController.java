package com.liu.system.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.system.constants.UserConstants;
import com.liu.system.dao.SysMenu;
import com.liu.system.dao.SysRole;
import com.liu.system.dao.SysUser;
import com.liu.system.service.SysUserService;
import com.liu.system.service.relation.SysRoleAndMenuService;
import com.liu.system.service.relation.SysUserAndRoleService;
import com.liu.system.vo.UserInfo;
import com.liu.system.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息控制层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysuserService;

    @Autowired
    private SysUserAndRoleService userAndRoleService;

    @Autowired
    private SysRoleAndMenuService roleAndMenuService;

    /**
     * 查询 用户信息 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param sysuser   用户信息对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "deptId", description = "部门ID"),
            @Parameter(name = "status", description = "状态"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "startTime", description = "开始时间"),
            @Parameter(name = "endTime", description = "结束时间"),
            @Parameter(name = "keywords", description = "关键词")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "status", required = false) Long status,
            @RequestParam(value = "sortRules", defaultValue = "user_id") String sortRules,
            @RequestParam(value = "startTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date startTime,
            @RequestParam(value = "endTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date endTime,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            @RequestParam(value = "keywords", required = false) String keywords) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        SysUser user = new SysUser();
        if (deptId != null && deptId != 0) {
            user.setDeptId(deptId);
        }
        if (status != null) {
            user.setStatus(String.valueOf(status));
        }
        if (StrUtil.isNotEmpty(keywords)) {
            user.setUserName(keywords);
            user.setNickName(keywords);
        }
        HashMap<String, Object> param = new HashMap<>(2);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        user.setParams(param);
        // 2024/4/12/21:05 对于时间区间还未完成....  状态、关键词搜索 有些问题 如果关键词有内容状态为1 则会出现以关键词为准的数据 这个是因为关键词数据时以 or 查询的 or .. and .. 不管怎么样都会有数据

        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysUser> list = sysuserService.selectSysUserList(user);
        PageInfo<SysUser> pageInfo = new PageInfo<>();
        long total = pageInfo.getTotal();
        clearPage();
        List<UserVo> userVos = list.stream().map(this::sysToUserVo).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("list", userVos);
        map.put("total", total);
        return R.success(map);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysUser sysuser) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        List<SysUser> list = sysuserService.selectSysUserList(sysuser);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.exportExcel(response, list, "用户信息数据", excludeColumnFiledNames);
    }

    @Operation(summary = "获取当前登录的用户信息")
    @GetMapping("/me")
    public R<UserInfo> getMe(Principal principal) {
        String username = principal.getName();
        SysUser sysUser = sysuserService.getItemByUserName(username);
        if (sysUser == null) {
            return R.fail("无有效信息");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(sysUser.getUserId());
        userInfo.setUsername(sysUser.getUserName());
        userInfo.setNickname(sysUser.getNickName());
        userInfo.setDeptId(sysUser.getDeptId());
        userInfo.setAvatar(sysUser.getAvatar());
        // 获取 RoleKey
        List<SysRole> roleList = userAndRoleService.getRoleByUserId(sysUser.getUserId());
        List<String> roles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
        userInfo.setRoles(roles);
        // 获取 Perms 权限标识
        Set<String> perms = new HashSet<>();
        for (SysRole sysRole : roleList) {
            List<SysMenu> menus = roleAndMenuService.selectMenuListByRoleId(sysRole.getRoleId());
            for (SysMenu menu : menus) {
                perms.add(menu.getPerms());
            }
        }
        userInfo.setPerms(perms);
        return R.success(userInfo);
    }


    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/{userId}")
    public R<UserVo> getInfo(
            @Parameter(name = "userId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("userId") Long userId) {
        SysUser sysUser = sysuserService.selectSysUserByUserId(userId);
        if (ObjUtil.isEmpty(sysUser)) {
            return R.fail("用户不存在");
        }
        // 角色
        List<Long> sysRoleList = userAndRoleService.getRoleByUserId(sysUser.getUserId())
                .stream().map(SysRole::getRoleId).collect(Collectors.toList());
        UserVo userVo = sysToUserVo(sysUser);
        userVo.setRoleIds(sysRoleList);
        return R.success(userVo);
    }


    /**
     * 新增 用户信息
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@Valid @RequestBody UserVo userVo, HttpServletRequest request) {
        SysUser sysUser = userVoToSys(userVo);
        sysUser.setCreateBy(SecurityUtils.getCurrentUser(request));
        sysuserService.insert(sysUser);
        // 查询该用户
        Long userId = sysuserService.getItemByUserName(sysUser.getUserName()).getUserId();
        // 添加 角色列表
        List<Long> roleIds = userVo.getRoleIds();
        if (CollUtil.isEmpty(roleIds)) {
            return R.success();
        }
        userAndRoleService.add(userId, roleIds);
        return R.success();
    }


    /**
     * 修改 用户信息
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@Valid @RequestBody UserVo userVo, HttpServletRequest request) {
        SysUser sysUser = userVoToSys(userVo);
        sysUser.setUpdateBy(SecurityUtils.getCurrentUser(request));
        sysuserService.update(sysUser);
        // 更新 角色列表
        List<Long> roleIds = userVo.getRoleIds();
        if (CollUtil.isEmpty(roleIds)) {
            return R.success();
        }
        userAndRoleService.update(sysUser.getUserId(), roleIds);
        return R.success();
    }

    /**
     * 修改 用户状态
     */
    @Operation(summary = "修改用户状态")
    @PatchMapping("/update/status/{userId}")
    public R<Integer> update(@PathVariable("userId") Long userId,
                             @RequestParam("status") Integer status, HttpServletRequest request) {
        SysUser sysUser = sysuserService.selectSysUserByUserId(userId);
        sysUser.setStatus(status == 0 ? "0" : "1");
        sysUser.setUpdateBy(SecurityUtils.getCurrentUser(request));
        sysuserService.update(sysUser);
        return R.success();
    }

    /**
     * 修改 密码
     */
    @Operation(summary = "修改用户密码")
    @PatchMapping("/update/pwd/{userId}")
    public R<Integer> update(@PathVariable("userId") Long userId,
                             @RequestParam("password") String password, HttpServletRequest request) {
        SysUser sysUser = sysuserService.selectSysUserByUserId(userId);
        // 验证后 再更新密码
        if (StrUtil.isEmpty(password)) {
            return R.fail("密码不能为空!");
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.fail("密码长度必须在5到20个字符之间");
        }
        SecurityUtils.encryptPassword(password);
        sysUser.setUpdateBy(SecurityUtils.getCurrentUser(request));
        sysuserService.update(sysUser);
        return R.success();
    }


    /**
     * 删除 用户信息
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{userIds}")
    public R<Integer> delete(@PathVariable("userIds") Long[] userIds) {
        return R.success(sysuserService.delete(userIds));
    }


    private SysUser userVoToSys(UserVo userVo) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVo.getId());
        sysUser.setDeptId(userVo.getDeptId());
        sysUser.setUserName(userVo.getUsername());
        sysUser.setNickName(userVo.getNickname());
        sysUser.setEmail(userVo.getEmail());
        sysUser.setPhone(userVo.getMobile());
        sysUser.setSex(String.valueOf(userVo.getGender()));
        sysUser.setAvatar(userVo.getAvatar());
        sysUser.setStatus(userVo.getStatus() == 0 ? "0" : "1");
//        sysUser.setUserType("00");
//        sysUser.setPassword();
//        sysUser.setLoginIp();
//        sysUser.setLoginDate();
//        sysUser.setCreateBy();
//        sysUser.setCreateTime();
//        sysUser.setUpdateBy();
//        sysUser.setUpdateTime();
//        sysUser.setRemark();
//        sysUser.setIsDelete();
        return sysUser;
    }

    private UserVo sysToUserVo(SysUser sysUser) {
        UserVo userVo = new UserVo();
        userVo.setId(sysUser.getUserId());
        userVo.setDeptId(sysUser.getDeptId());
        userVo.setUsername(sysUser.getUserName());
        userVo.setNickname(sysUser.getNickName());
        userVo.setMobile(sysUser.getPhone());
        userVo.setGender(Long.valueOf(sysUser.getSex()));
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setEmail(sysUser.getEmail());
        userVo.setStatus(Objects.equals(sysUser.getStatus(), "0") ? 0 : 1);
        userVo.setCreateTime(sysUser.getCreateTime());
        userVo.setRoleIds(null);
        return userVo;
    }
}
