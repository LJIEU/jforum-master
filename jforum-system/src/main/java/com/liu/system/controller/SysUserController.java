package com.liu.system.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.liu.core.constant.UserConstants;
import com.liu.core.controller.BaseController;
import com.liu.core.model.LoginUser;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.db.entity.SysMenu;
import com.liu.db.entity.SysRole;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import com.liu.db.service.relation.SysRoleAndMenuService;
import com.liu.db.service.relation.SysUserAndRoleService;
import com.liu.db.vo.UserInfo;
import com.liu.db.vo.UserVo;
import com.liu.system.config.excel.handler.UserWriteHandler;
import com.liu.system.config.excel.listener.UserDataListener;
import com.liu.system.config.excel.temple.UserTemple;
import com.liu.system.context.UserDataListenerHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息控制层 sys_user
 *
 * @author JIE
 * @since 2024-04-03
 */
@Tag(name = "用户管理模块")
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
     * @param keywords  关键词查询 目前只支持 nickName username
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
            // TODO 2024/4/15/23:33  如果是 父级部门[1] --> 其子部门[2,3,4] 则直接 查询其 子部门 和 父部门 ID ==> [1,2,3,4]
            // 这里就不再是 setDeptID ==> 而是 dept_id in(1,2,3,4)
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
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        List<UserVo> userVos = list.stream().map(this::sysToUserVo).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("list", userVos);
        map.put("total", total);
        return R.success(map);
    }


    /**
     * 导出数据 Excel格式
     */
    @SuppressWarnings("all")
    @Operation(summary = "导出数据 Excel格式")
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
    @GetMapping("/export")
    public void export(
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
            @RequestParam(value = "keywords", required = false) String keywords, HttpServletResponse response) {
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
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<SysUser> list = sysuserService.selectSysUserList(user);

        // 忽略字段  @ExcelIgnore 也可以完成
//        Set<String> excludeColumnFiledNames = new HashSet<>();
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    @Operation(summary = "用户导入模板")
    @GetMapping("/template")
    public void template(HttpServletResponse response) {
/*        List<List<String>> initHead = new ArrayList<>();
        List<List<Object>> initList = new ArrayList<>();

        // 头部信息
        String[] headers = {"用户ID", "部门", "用户账号", "用户昵称", "用户类型\n(00系统用户)",
                "用户邮箱", "手机号码", "用户性别", "头像地址", "账号状态", "备注"};
        for (String header : headers) {
            List<String> head = new ArrayList<>();
            head.add(header);
            initHead.add(head);
        }
        // 数据
        String[] initData = {"不需要填写自动生成!", "部门 请根据提示填写", "xxx", "xxx", "xxx",
                "xxx@qq.com", "xxx", "性别 请根据提示填写", "http://xxx", "状态 请根据提示填写", "用户模板样式"};

        List<Object> data = Arrays.asList(initData);
        initList.add(data);*/
        List<UserTemple> initList = new ArrayList<>();
        UserTemple userTemple = new UserTemple();
        userTemple.setUserId("不需要填写自动生成!!");
        userTemple.setDeptId(-1L);
        userTemple.setUserName("xxx");
        userTemple.setNickName("xxx");
        userTemple.setUserType("temple");
        userTemple.setEmail("xxx");
        userTemple.setPhone("xxx");
        userTemple.setSex("temple");
        userTemple.setAvatar("http://xxx");
        userTemple.setStatus("temple");
        initList.add(userTemple);
        ExcelUtil.exportExcelTemplate(response, "用户", new UserWriteHandler(), UserWriteHandler.style(),
                UserTemple.class, initList);
    }


    @Operation(summary = "获取当前登录的用户信息")
    @GetMapping("/me")
    public R<UserInfo> getMe(HttpServletRequest request) {
//        String username = principal.getName();
        String username = SecurityUtils.currentUsername(request);
        SysUser sysUser = sysuserService.getItemByUserName(username);
        if (sysUser == null) {
            return R.fail("无有效信息");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setCurrRole(loginUser.getCurrRole());
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
        String currRoleName = SecurityUtils.currRoleName();
        for (SysRole sysRole : roleList) {
            if (sysRole.getRoleName().equals(currRoleName)) {
                List<SysMenu> menus = roleAndMenuService.selectMenuListByRoleId(sysRole.getRoleId());
                for (SysMenu menu : menus) {
                    perms.add(menu.getPerms());
                }
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
     * 文件上传
     */
    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("@authority.hasPermission('sys:user:add')")
    public R<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(),
                UserTemple.class,
                new UserDataListener()).sheet("模板").doRead();
        return R.success(UserDataListenerHolder.getContext());
    }


    /**
     * 新增 用户信息
     */
    @Operation(summary = "新增用户")
    @PostMapping("/add")
    @PreAuthorize("@authority.hasPermission('sys:user:add')")
    public R<Integer> add(@Valid @RequestBody UserVo userVo, HttpServletRequest request) {
        SysUser sysUser = userVoToSys(userVo);
        sysUser.setCreateBy(SecurityUtils.currentUsername(request));
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
    @Operation(summary = "修改用户")
    @PutMapping("/update")
    @PreAuthorize("@authority.hasPermission('sys:user:edit')")
    public R<Integer> update(@Valid @RequestBody UserVo userVo, HttpServletRequest request) {
        SysUser sysUser = userVoToSys(userVo);
        sysUser.setUpdateBy(SecurityUtils.currentUsername(request));
        sysuserService.update(sysUser);
        // 更新 角色列表
        List<Long> roleIds = userVo.getRoleIds();
        userAndRoleService.update(sysUser.getUserId(), roleIds, SecurityUtils.currentUsername(request));
        return R.success();
    }

    /**
     * 修改 用户状态
     */
    @Operation(summary = "修改用户状态")
    @PatchMapping("/update/status/{userId}")
    @PreAuthorize("@authority.hasPermission('sys:user:edit')")
    public R<Integer> update(@PathVariable("userId") Long userId,
                             @RequestParam("status") Integer status, HttpServletRequest request) {
        SysUser sysUser = sysuserService.selectSysUserByUserId(userId);
        sysUser.setStatus(status == 0 ? "0" : "1");
        sysUser.setUpdateBy(SecurityUtils.currentUsername(request));
        sysuserService.update(sysUser);
        return R.success();
    }

    /**
     * 修改 密码
     */
    @Operation(summary = "修改用户密码")
    @PatchMapping("/update/pwd/{userId}")
    @PreAuthorize("@authority.hasPermission('sys:user:reset_pwd')")
    public R<Integer> update(@PathVariable("userId") Long userId,
                             @RequestParam("password") String password, HttpServletRequest request) {
        SysUser sysUser = sysuserService.selectSysUserByUserId(userId);
        // 验证后 再更新密码
        if (StrUtil.isEmpty(password)) {
            return R.fail("密码不能为空!");
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.fail("密码长度必须在5到20个字符之间");
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setUpdateBy(SecurityUtils.currentUsername(request));
        sysuserService.update(sysUser);
        return R.success();
    }


    /**
     * 删除 用户信息
     * /delete/1,2,3
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{userIds}")
    @PreAuthorize("@authority.hasPermission('sys:user:delete')")
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
