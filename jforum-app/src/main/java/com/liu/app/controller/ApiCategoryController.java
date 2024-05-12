package com.liu.app.controller;

import com.liu.core.converter.LevelConverter;
import com.liu.core.result.R;
import com.liu.core.utils.LevelUtils;
import com.liu.db.converter.level.CategoryConverter;
import com.liu.db.entity.Category;
import com.liu.db.service.CategoryService;
import com.liu.db.vo.level.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 10:32
 */
@Tag(name = "分类")
@RestController
@RequestMapping("/app/category")
public class ApiCategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取所有分类【父子级】")
    @GetMapping("/api/list")
    public R<List<Level>> list() {
        List<Category> categoryList = categoryService.selectCategoryList(null);
        LevelConverter<Category, Level> converter = new CategoryConverter();
        List<Level> levels = LevelUtils.buildTree(categoryList, converter, 0L);
        return R.success(levels);
    }

}
