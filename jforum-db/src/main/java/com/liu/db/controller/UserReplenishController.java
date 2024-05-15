//package com.liu.db.controller;
//
//import com.liu.db.core.controller.BaseController;
//import com.liu.db.core.converter.TreeConverter;
//import com.liu.db.core.result.R;
//import com.liu.db.core.utils.ExcelUtil;
//import com.liu.db.core.utils.TreeUtils;
//import com.liu.db.entity.UserReplenish;
//import com.liu.db.service.UserReplenishService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//import java.util.List;
//
///**
// * 用户补充信息控制层 user_replenish
// *
// * @author JIE
// * @since 2024-05-15
// */
//@Tag(name = "用户补充信息")
//@RestController
//@RequestMapping("/system/userreplenish")
//public class UserReplenishController extends BaseController {
//
//    @Autowired
//    private UserReplenishService userreplenishService;
//
//    /**
//     * 查询 用户补充信息 列表
//     *
//     * @param pageNum   当前页码
//     * @param pageSize  页记录数
//     * @param sortRules 排序规则
//     * @param isDesc    是否逆序
//     * @param userreplenish  用户补充信息对象
//     * @return 返回 分页 查询结果
//     */
//    @Operation(summary = "分页查询")
//    @Parameters({
//            @Parameter(name = "pageNum", description = "当前页", example = "1"),
//            @Parameter(name = "pageSize", description = "页大小", example = "10"),
//            @Parameter(name = "sortRules", description = "排序规则"),
//            @Parameter(name = "isDesc", description = "是否逆序排序"),
//            @Parameter(name = "userreplenish", description = "实体参数")
//    })
//    @GetMapping("/list")
//    public R<List<UserReplenish>> list(
//            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//            @RequestParam(value = "sortRules", defaultValue = "user_id") String sortRules,
//            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
//        UserReplenish userreplenish) {
//        startPage(pageNum, pageSize, sortRules, isDesc);
//        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
//        List<UserReplenish> list = userreplenishService.selectUserReplenishList(userreplenish);
//        clearPage();
//        return R.success(list);
//    }
//
//
//    /**
//     * 导出数据 Excel格式
//     */
//    @Operation(summary = "导出数据 Excel格式")
//    @GetMapping("/export")
//    public void export(HttpServletResponse response, UserReplenish userreplenish) {
//        // 忽略字段
//        Set<String> excludeColumnFiledNames = new HashSet<>();
//            excludeColumnFiledNames.add("createBy");
//            excludeColumnFiledNames.add("createTime");
//            excludeColumnFiledNames.add("updateBy");
//            excludeColumnFiledNames.add("remark");
//            excludeColumnFiledNames.add("params");
//        List<UserReplenish> list = userreplenishService.selectUserReplenishList(userreplenish);
//        ExcelUtil<UserReplenish> util = new ExcelUtil<>(UserReplenish. class);
//        util.exportExcel(response, list, "用户补充信息数据", excludeColumnFiledNames);
//    }
//
//
//    /**
//     * 获取 用户补充信息 详细信息
//     */
//    @Operation(summary = "根据ID获取详细信息")
//    @GetMapping("/{userId}")
//    public R<UserReplenish> getInfo(
//            @Parameter(name = "userId", description = "ID", in = ParameterIn.PATH)
//            @PathVariable("userId") Long userId) {
//        return R.success(userreplenishService.selectUserReplenishByUserId(userId));
//    }
//
//
//    /**
//     * 新增 用户补充信息
//     */
//    @Operation(summary = "新增")
//    @PostMapping("/add")
//    public R<Integer> add(@RequestBody UserReplenish userreplenish) {
//        return R.success(userreplenishService.insert(userreplenish));
//    }
//
//
//    /**
//     * 修改 用户补充信息
//     */
//    @Operation(summary = "修改")
//    @PutMapping("/update")
//    public R<Integer> update(@RequestBody UserReplenish userreplenish) {
//        return R.success(userreplenishService.update(userreplenish));
//    }
//
//
//    /**
//     * 删除 用户补充信息
//     * /delete/1,2,3
//     */
//    @Operation(summary = "删除")
//    @DeleteMapping("/delete/{userIds}")
//    public R<Integer> delete(@PathVariable("userIds") Long[] userIds) {
//        return R.success(userreplenishService.delete(userIds));
//    }
//
//
//}
