package com.liu.app.controller;

import com.liu.core.result.R;
import com.liu.db.nodb.document.TagDocument;
import com.liu.db.service.nodb.document.TagService;
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
 * @since 2024/05/12 10:54
 */
@Tag(name = "文章标签")
@RequestMapping("/app/tag")
@RestController
public class ApiTagController {
    @Autowired
    private TagService tagService;

    @Operation(summary = "获取所有标签")
    @GetMapping("/api/list")
    public R<List<TagDocument>> list() {
        return R.success(tagService.list());
    }
}
