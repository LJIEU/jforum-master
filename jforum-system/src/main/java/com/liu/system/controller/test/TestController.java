package com.liu.system.controller.test;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.liu.core.annotation.Log;
import com.liu.core.config.dynamic.DataSource;
import com.liu.core.config.repeat.RepeatSubmit;
import com.liu.core.constant.enume.BusinessType;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.core.controller.BaseController;
import com.liu.core.model.BaseUser;
import com.liu.core.model.LoginUser;
import com.liu.core.result.R;
import com.liu.security.service.JwtTokenService;
import com.liu.db.entity.SysConfig;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Description: 用于测试
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 10:10
 */
@Tag(name = "测试模块")
@RequestMapping("/test")
@RestController
public class TestController extends BaseController {

    @GetMapping("/index")
    @Operation(summary = "首页")
    public String index() {
        return "Hello,JForum! ❤";
    }


    /**
     * 测试:http://localhost/test?date=2021-01-21%2019:08:20  %20是空格
     * 这个的日期格式符合 日期编辑器中的 yyyy-MM-dd HH:mm:ss 它会将其转换成 Date 格式
     * 然后就会进入下面的方法 最后返回给前端一个 Date 类型格式的 时间 {date=Thu Jan 21 19:08:20 CST 2021}
     * 测试:http://localhost/test?date=2021/01/21%209/08/20 如果是这个链接则不满足自定义日期编辑器
     * 就会返回一个 400 错误 ....  Could not parse date: Unparseable date: "2021/01/21 9/08/20"
     * 使用 升级版后 里面的解析模板里添加了 yyyy/MM/dd HH/mm/ss 就可以对其格式进行解析了
     */
    @Operation(summary = "测试格式转换器")
    @GetMapping("/test")
    public String testFormatDate(@RequestParam("date") Date date) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("date", date);
        return map.toString();
    }


    /**
     * {
     * "code": 200,
     * "message": "成功~",
     * "data": "Thu Jan 21 09:08:20 CST 2021"
     * }
     */
    @Operation(summary = "数据结果封装")
    @GetMapping("/test2")
    public R<String> testFormatDate2(@RequestParam("date") Date date) {
        return R.success(date.toString());
    }


    /**
     * {
     * "code": 500,
     * "message": "服务器异常",
     * "data": null
     * }
     */
    @Operation(summary = "日志注解测试 [异常]")
    @Log(describe = "测试 日志功能注解", businessType = BusinessType.OTHER)
    @GetMapping("/test3")
    public void testError() {
        throw new RuntimeException("发现异常");
    }

    @Operation(summary = "日志注解测试 [正常]")
    @Log(describe = "日志测试", businessType = BusinessType.SELECT)
    @GetMapping("/test4")
    public R<String> testLog(@RequestParam(name = "message") String message) {
        return R.success(message);
    }

    @Lazy
    @Autowired
    private JwtTokenService jwtTokenService;

    @Operation(summary = "获取Token")
    @GetMapping("/test5")
    public R<String> getToken(@Parameter(name = "username", description = "用户名")
                              @RequestParam("username") String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        HashSet<String> permission = new HashSet<>();
        permission.add("*:*:*");
        LoginUser loginUser = new LoginUser(1L, new BaseUser(
                sysUser.getUserId(), sysUser.getPassword(), sysUser.getUserName(), sysUser.getStatus()), permission);
        String token = jwtTokenService.createToken(loginUser);
        return R.success(token);
    }

    @Operation(summary = "权限测试[需要权限才可访问]")
    @PreAuthorize("@authority.hasPermission('system:menu:list')")
    @GetMapping("/test6")
    public String preAuthorize() {
        return "权限管理";
    }

    @Operation(summary = "权限测试[无权限控制]")
    @GetMapping("/test7")
    public String preAuthorize2() {
        return "权限管理,测试可访问的..无权限..无需验证的";
    }

    @ApiOperationSupport(order = 100)
    @Operation(summary = "测试防止重复提交")
    @RepeatSubmit
    @PostMapping("/test8")
    public R<String> repeat(
            @RequestBody SysUser sysUser) {
        return R.success(sysUser.getUserName());
    }

    @Autowired
    private SysConfigService configService;

    @Operation(summary = "多数据源切换[主]")
    @GetMapping("/test9/1")
    @DataSource(DataSourceType.MASTER)
    public R<List<SysConfig>> multipleDataSources1() {
        List<SysConfig> sysConfigs = configService.selectSysConfigList(null);
        return R.success("多数据源", sysConfigs);
    }

    @Operation(summary = "多数据源切换[从]")
    @GetMapping("/test9/2")
    @DataSource(DataSourceType.SLAVE)
    public R<List<SysConfig>> multipleDataSources2() {
        List<SysConfig> sysConfigs = configService.selectSysConfigList(null);
        return R.success("多数据源", sysConfigs);
    }


}
