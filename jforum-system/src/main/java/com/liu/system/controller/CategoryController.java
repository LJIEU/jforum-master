package com.liu.system.controller;

import com.liu.core.controller.BaseController;
import com.liu.core.converter.LevelConverter;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.LevelUtils;
import com.liu.db.converter.level.CategoryConverter;
import com.liu.db.entity.Category;
import com.liu.db.service.CategoryService;
import com.liu.db.vo.level.Level;
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
 * 帖子分类控制层 category
 *
 * @author JIE
 * @since 2024-04-23
 */
@Tag(name = "分类管理")
@RestController
@RequestMapping("/sys/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询 帖子分类 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param category  帖子分类对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "category", description = "实体参数")
    })
    @GetMapping("/list")
    public R<List<Category>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortRules", defaultValue = "category_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            Category category) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        List<Category> list = categoryService.selectCategoryList(category);
        clearPage();
        return R.success(list);
    }

    /**
     * 层次结构 数据
     */
    @Operation(summary = "获取层次结构")
    @GetMapping("/level")
    public R<List<Level>> list(Category category) {
        List<Category> list = categoryService.selectCategoryList(category);
        // 整理成 层次结构
        LevelConverter<Category, Level> converter = new CategoryConverter();
        List<Level> levels = LevelUtils.buildTree(list, converter, 0L);
        return R.success(levels);
    }

    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, Category category) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("isDelete");
        excludeColumnFiledNames.add("params");
        List<Category> list = categoryService.selectCategoryList(category);
        ExcelUtil<Category> util = new ExcelUtil<>(Category.class);
        util.exportExcel(response, list, "帖子分类数据", excludeColumnFiledNames);
    }


    /**
     * 获取 帖子分类 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{categoryId}")
    public R<Category> getInfo(
            @Parameter(name = "categoryId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("categoryId") Long categoryId) {
        return R.success(categoryService.selectCategoryByCategoryId(categoryId));
    }


    /**
     * 新增 帖子分类
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Integer> add(@RequestBody Category category) {
        return R.success(categoryService.insert(category));
    }


    /**
     * 修改 帖子分类
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody Category category) {
        return R.success(categoryService.update(category));
    }


    /**
     * 删除 帖子分类
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{categoryIds}")
    public R<Integer> delete(@PathVariable("categoryIds") Long[] categoryIds) {
        return R.success(categoryService.delete(categoryIds));
    }


}
