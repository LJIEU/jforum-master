package com.liu.system.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.liu.core.controller.BaseController;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import com.liu.db.entity.SysUser;
import com.liu.db.service.CategoryService;
import com.liu.db.service.PostService;
import com.liu.db.service.SysUserService;
import com.liu.db.service.relation.PostAndCategoryService;
import com.liu.db.vo.PostVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子控制层 post
 *
 * @author JIE
 * @since 2024-04-23
 */
@Tag(name = "帖子管理")
@RestController
@RequestMapping("/sys/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询 帖子 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param post      帖子对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "categoryId", description = "分类ID"),
            @Parameter(name = "status", description = "状态"),
            @Parameter(name = "startTime", description = "开始时间"),
            @Parameter(name = "endTime", description = "结束时间"),
            @Parameter(name = "keywords", description = "关键词"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "post", description = "实体参数")
    })
    @GetMapping("/list")
    public R<Map<String, Object>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date startTime,
            @RequestParam(value = "endTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                    Date endTime,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "sortRules", defaultValue = "post_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            Post post) {
        HashMap<String, Object> map = new HashMap<>(2);
        if (status != null) {
            post.setState(String.valueOf(status));
        }
        if (StrUtil.isNotEmpty(keywords)) {
            post.setTitle(keywords);
        }
        HashMap<String, Object> param = new HashMap<>(2);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        post.setParams(param);
        if (categoryService.selectCategoryByCategoryId(categoryId) == null) {
            startPage(pageNum, pageSize, sortRules, isDesc);
            List<Post> posts = postService.selectPostList(post);
            PageInfo<Post> pageInfo = new PageInfo<>(posts);
            map.put("total", pageInfo.getTotal());
            map.put("data", posts.stream().map(this::postToVo).collect(Collectors.toList()));
            return R.success(map);
        }
        // 获取该 categoryId 是否是有上级 ==> 将其上级列表获取出
        Category category = categoryService.selectCategoryByCategoryId(categoryId);
        if (category != null) {
            // 说明是 根级
            List<Post> posts = new ArrayList<>();
            PostAndCategoryService postAndCategoryService = SpringUtils.getBean(PostAndCategoryService.class);
            if (category.getPid() == 0) {
                List<Long> categoryIds = categoryService.getChildIdByPid(category.getCategoryId());
                categoryIds.add(categoryId);
                for (Long id : categoryIds) {
                    posts.addAll(postAndCategoryService.selectPostByCategoryId(id));
                }
            } else {
                posts = postAndCategoryService.selectPostByCategoryId(categoryId);
            }
            // 整理为分页数据
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, posts.size());
            List<PostVo> data = new ArrayList<>();
            for (int i = start; i < end; i++) {
                data.add(postToVo(posts.get(i)));
            }
            map.put("total", posts.size());
            map.put("data", data);
            return R.success(map);
        }
        return R.success();
    }


    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, Post post) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("createBy");
        excludeColumnFiledNames.add("updateBy");
        excludeColumnFiledNames.add("remark");
        excludeColumnFiledNames.add("params");
        List<Post> list = postService.selectPostList(post);
        ExcelUtil<Post> util = new ExcelUtil<>(Post.class);
        util.exportExcel(response, list, "帖子数据", excludeColumnFiledNames);
    }


    /**
     * 获取 帖子 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{postId}")
    public R<PostVo> getInfo(
            @Parameter(name = "postId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("postId") String postId) {
        Post post = postService.selectPostByPostId(postId);
        return R.success(postToVo(post));
    }

    /**
     * 修改 帖子
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody Post post) {
        return R.success(postService.update(post));
    }


    /**
     * 删除 帖子
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{postIds}")
    public R<Integer> delete(@PathVariable("postIds") String[] postIds) {
        return R.success(postService.delete(postIds));
    }


    private PostVo postToVo(Post post) {
        PostVo vo = new PostVo();
        Category category = SpringUtils.getBean(PostAndCategoryService.class).selectCategoryByPostId(post.getPostId());
        SysUser user = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(post.getUserId());
        vo.setId(post.getPostId());
        vo.setNickname(user.getNickName());
        vo.setReleaseTime(post.getUpdateTime());
        // TODO 2024/4/25/20:20 模拟数据 后期要与数据库实际
        vo.setLikeNum(RandomUtil.randomInt(1, 1000));
        vo.setCommentNum(RandomUtil.randomInt(2, 1000));
        vo.setOverallRating((int) (new Random().nextDouble() * 10));
        vo.setTitle(post.getTitle());
        // TODO 2024/4/24/11:24 用户删除时 对应的 帖子也需要 逻辑删除
        vo.setUsername(user.getUserName());
        vo.setCategory(category.getCategoryId());
        vo.setContent(post.getContent());
        vo.setState(Integer.valueOf(post.getState()));
        // 根据分类关联表 获取 类别名称
        vo.setCategoryName(category.getName());
        vo.setPlace(post.getIpAddress());
        vo.setCreateTime(post.getCreateTime());
        vo.setType(post.getType());
        return vo;
    }

}
