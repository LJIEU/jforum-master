package com.liu.app.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.db.entity.Like;
import com.liu.db.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 点赞控制层 like
 *
 * @author JIE
 * @since 2024-05-11
 */
@Tag(name = "点赞")
@RestController
@RequestMapping("/app/like")
public class LikeController extends BaseController {

    @Autowired
    private LikeService likeService;

    /**
     * 查询 点赞 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param like  点赞对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "like", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<Like>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "like_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
        Like like) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<Like> list = likeService.selectLikeList(like);
        clearPage();
        return R.success(list);
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, Like like) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
            excludeColumnFiledNames.add("updateBy");
            excludeColumnFiledNames.add("updateTime");
            excludeColumnFiledNames.add("remark");
            excludeColumnFiledNames.add("isDelete");
            excludeColumnFiledNames.add("params");
        List<Like> list = likeService.selectLikeList(like);
        ExcelUtil<Like> util = new ExcelUtil<>(Like. class);
        util.exportExcel(response, list, "点赞数据", excludeColumnFiledNames);
    }


    /**
     * 获取 点赞 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{likeId}")
    public R<Like> getInfo(
            @Parameter(name = "likeId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("likeId") Long likeId) {
        return R.success(likeService.selectLikeByLikeId(likeId));
    }


    /**
     * 新增 点赞
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody Like like) {
        return R.success(likeService.insert(like));
    }


    /**
     * 修改 点赞
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody Like like) {
        return R.success(likeService.update(like));
    }


    /**
     * 删除 点赞
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{likeIds}")
    public R<Integer> delete(@PathVariable("likeIds") Long[] likeIds) {
        return R.success(likeService.delete(likeIds));
    }


}
