package com.liu.app.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import com.liu.core.config.repeat.RepeatSubmit;
import com.liu.core.constant.PostState;
import com.liu.core.controller.BaseController;
import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.result.R;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.Category;
import com.liu.db.entity.Post;
import com.liu.db.entity.SysUser;
import com.liu.db.service.CategoryService;
import com.liu.db.service.PostService;
import com.liu.db.service.SysUserService;
import com.liu.db.service.relation.PostAndCategoryService;
import com.liu.db.service.relation.PostAndTagService;
import com.liu.db.vo.PostVo;
import com.liu.db.vo.api.PostParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/10 21:06
 */
@Tag(name = "帖子模块")
@RequestMapping("/app/post/")
@RestController
public class ApiPostController extends BaseController {
    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SysUserService userService;

    /**
     * 分页查询文章列表。
     *
     * @param pageNum    页码，默认为1
     * @param pageSize   每页显示的数量，默认为10
     * @param categoryId 分类ID，可选参数，用于筛选指定分类下的文章
     * @param status     文章状态，可选参数，用于筛选指定状态的文章
     * @param startTime  查询开始时间，可选参数，格式为"yyyy-MM-dd"，按时间范围筛选文章
     * @param endTime    查询结束时间，可选参数，格式为"yyyy-MM-dd"，按时间范围筛选文章
     * @param keywords   关键词，可选参数，用于文章标题的检索
     * @param sortRules  排序规则，默认为"post_id"，用于指定文章排序方式
     * @param isDesc     是否降序，默认为false，用于指定排序顺序
     * @param post       文章实体，用于传递筛选条件，其中的状态、关键词等会用于查询条件
     * @return 返回一个包含文章列表和总数的地图，其中文章列表是转换为VO后的格式
     */
    @Operation(summary = "分页查询")
    @GetMapping("/api/postList/{pageSize}/{pageNum}")
    public R<Map<String, Object>> postList(@PathVariable(value = "pageNum") Integer pageNum,
                                           @PathVariable(value = "pageSize") Integer pageSize,
                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                           @RequestParam(value = "status", required = false) Integer status,
                                           @RequestParam(value = "startTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                                                   Date startTime,
                                           @RequestParam(value = "endTime", required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
                                                   Date endTime,
                                           @RequestParam(value = "keywords", required = false) String keywords,
                                           @RequestParam(value = "sortRules", defaultValue = "post_id") String sortRules,
                                           @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
                                           @RequestParam(value = "isMy", defaultValue = "false") Boolean isMy,
                                           Post post, HttpServletRequest request) {
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

        // 只能获取审核通过的[只限于首页帖子]
        post.setState(PostState.POST_PUBLISH);

        if (isMy) {
            SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
            if (user == null) {
                throw new UserNotExistsException();
            }
            // 查询 所有自己的帖子
            post.setState(null);
            post.setUserId(user.getUserId());
        }
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


    @Operation(summary = "查询帖子信息")
    @GetMapping("/api/{postId}")
    public R<PostVo> getInfo(@PathVariable("postId") String postId, HttpServletRequest request) {
        Post post = postService.selectPostByPostId(postId);
        if (post == null) {
            return R.fail("帖子不存在");
        }
        // 如果帖子的 状态不是公布的那就得进行过滤  只有自己可以查看自己的别人查看不了
        if (!PostState.POST_PUBLISH.equals(post.getState())) {
            SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
            if (user == null || !user.getUserId().equals(post.getUserId())) {
                return R.fail(5001, "未公开");
            }
        }
        PostVo postVo = postToVo(post);
        // 浏览记录+1
        postService.updateViews(post);
        return R.success(postVo);
    }

    @Operation(summary = "发布帖子")
    @GetMapping("/publish/{postId}")
    public R<String> publish(
            @PathVariable(value = "postId") String postId,
            HttpServletRequest request) {
        return updateState(postId, request);
    }

    @Operation(summary = "设为隐私")
    @GetMapping("/privacy/{postId}")
    public R<String> privacy(
            @PathVariable(value = "postId") String postId,
            HttpServletRequest request) {
        return updateState(postId, request);
    }

    private R<String> updateState(String postId, HttpServletRequest request) {
        // 需要判断是否是自己的帖子 并且状态为 5 隐私 才可以进行 发布
        Post post = postService.selectPostByPostId(postId);
        if (post == null) {
            return R.fail("帖子不存在");
        }
        SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null || !user.getUserId().equals(post.getUserId())) {
            return R.fail(5001, "未公开");
        }

        // 公开设置为隐私
        if (post.getState().equals(PostState.POST_PUBLISH)) {
            // 设置为 隐私
            post.setState(PostState.POST_PRIVACY);
            postService.update(post);
            return R.success();
        }
        // 隐私设置为 待审核
        if (post.getState().equals(PostState.POST_PRIVACY)) {
            // 设置为 待审核
            post.setState(PostState.POST_PENDING);
            postService.update(post);
            return R.success();
        }

        return R.success();
    }
//    @Operation(summary = "获取发帖者信息")
//    @GetMapping("/api/user/{postId}")
//    public R<Object> getUserByPostId(@PathVariable(value = "postId") String postId) {
//        Post post = postService.selectPostByPostId(postId);
//        if (post == null) {
//            return R.fail("获取失败");
//        }
//        SysUser user = userService.selectSysUserByUserId(post.getUserId());
//        // 2024/5/11/16:50 注意对结果只返回需要信息别全部显示给前端
//        return R.success(userToAuthorInfo(user));
//    }


    @Operation(summary = "创建帖子")
    @PutMapping("/createPost")
    @RepeatSubmit

    public R<String> createPost(@Validated @RequestBody PostParam param,
                                HttpServletRequest request) {
        SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new UserNotExistsException();
        }
        postService.createPost(param, request, user.getUserId());
        return R.success();
    }

    @Operation(summary = "修改帖子")
    @PostMapping("/update")
    @RepeatSubmit
    public R<String> updatePost(@Validated @RequestBody PostParam param,
                                HttpServletRequest request) {
        SysUser user = userService.getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new UserNotExistsException();
        }
        postService.update(param, request, user.getUserId());
        return R.success();
    }

    private PostVo postToVo(Post post) {
        PostVo vo = new PostVo();
        Category category = SpringUtils.getBean(PostAndCategoryService.class).selectCategoryByPostId(post.getPostId());
        List<String> tags = SpringUtils.getBean(PostAndTagService.class).selectTagsByPostId(post.getPostId());
        SysUser user = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(post.getUserId());
        vo.setId(post.getPostId());
        vo.setNickname(user.getNickName());
        vo.setAvatarUrl(user.getAvatar());
        // TODO 2024/5/11/16:56 以后会有一个 个人主页表  这些信息都是由这个表收集
        vo.setAuthorHome("https://www.zhihu.com/people/www.zhiyuan.com");
        vo.setSignature("我的个性签名" + RandomUtil.randomString(10));
        vo.setReleaseTime(post.getUpdateTime());
        vo.setViews(post.getViews());
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
        // 设置 标签
        vo.setTagIds(tags.toArray(new String[0]));
        return vo;
    }

}
