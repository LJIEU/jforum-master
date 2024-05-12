package com.liu.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.liu.core.controller.BaseController;
import com.liu.core.converter.TreeConverter;
import com.liu.core.excption.user.UserNotExistsException;
import com.liu.core.result.R;
import com.liu.core.utils.ExcelUtil;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.core.utils.TreeUtils;
import com.liu.db.converter.tree.CommentConverter;
import com.liu.db.entity.Comment;
import com.liu.db.entity.SysUser;
import com.liu.db.service.CommentService;
import com.liu.db.service.SysUserService;
import com.liu.db.vo.CommentVo;
import com.liu.db.vo.api.AuthorInfo;
import com.liu.db.vo.api.CommentParams;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * 评论控制层 comment
 *
 * @author JIE
 * @since 2024-05-11
 */
@Tag(name = "评论")
@RestController
@RequestMapping("/app/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;


    /**
     * 查询 评论 列表
     *
     * @param pageNum   当前页码
     * @param pageSize  页记录数
     * @param sortRules 排序规则
     * @param isDesc    是否逆序
     * @param comment   评论对象
     * @return 返回 分页 查询结果
     */
    @Operation(summary = "分页查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "当前页", example = "1"),
            @Parameter(name = "pageSize", description = "页大小", example = "10"),
            @Parameter(name = "postId", description = "帖子ID"),
            @Parameter(name = "sortRules", description = "排序规则"),
            @Parameter(name = "isDesc", description = "是否逆序排序"),
            @Parameter(name = "comment", description = "实体参数")
    })
    @GetMapping("/api/list")
    public R<List<CommentVo>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "postId", required = true) String postId,
            @RequestParam(value = "sortRules", defaultValue = "comment_id") String sortRules,
            @RequestParam(value = "isDesc", defaultValue = "false") Boolean isDesc,
            Comment comment) {
        startPage(pageNum, pageSize, sortRules, isDesc);
        // 获取到数据 进行整理[当前页码,页记录数,总页数,查询总条数,数据]
        // 该分页是查询所有 root 节点数据
        comment.setCommentPid(0L);
        comment.setPostId(postId);
        List<Comment> pageList = commentService.selectCommentList(comment);
        Stack<CommentVo> stack = new Stack<>();
        List<CommentVo> comments = new ArrayList<>();
        for (Comment v : pageList) {
            CommentVo vo = this.commentToVo(v);
            comments.add(vo);
            // 因为Java是引用传参 所以这里后续改变的值 comments 集合里面指向的对应的值也会改变
            stack.push(vo);
        }
        // 然后整理其子数据
        while (!stack.isEmpty()) {
            CommentVo curr = stack.pop();
            // 获取子节点
            Comment temp = new Comment();
            temp.setCommentPid(curr.getCommentId());
            temp.setPostId(postId);
            List<Comment> selectCommentList = commentService.selectCommentList(temp);
            if (selectCommentList != null || !selectCommentList.isEmpty()) {
                // 继续向下查询
                curr.setChildren(new ArrayList<>());
                for (Comment subComment : selectCommentList) {
                    CommentVo subVo = this.commentToVo(subComment);
                    curr.getChildren().add(subVo);
                    stack.push(subVo);
                }
            }

        }
        return R.success(comments);
    }

    /**
     * 树型结构 数据
     */
    @Operation(summary = "获取树型结构")
    @GetMapping("/tree")
    public R<List<CommentVo>> list(Comment comment) {
        List<Comment> list = commentService.selectCommentList(comment);
        // 整理成 树型结构
        TreeConverter<CommentVo> converter = new CommentConverter();
        List<CommentVo> commentVos = list.stream().map(this::commentToVo).collect(Collectors.toList());
        List<CommentVo> treeList = TreeUtils.convertTree(commentVos, converter);
        if (CollUtil.isEmpty(treeList)) {
            return R.success(commentVos);
        } else {
            return R.success(treeList);
        }
    }

    private CommentVo commentToVo(Comment comment) {
        CommentVo commentVo = new CommentVo();
        commentVo.setCommentId(comment.getCommentId());
        commentVo.setCommentPid(comment.getCommentPid());
        commentVo.setUserId(comment.getUserId());
        commentVo.setPostId(comment.getPostId());
        commentVo.setContent(comment.getContent());
        commentVo.setIp(comment.getIp());
        commentVo.setCity(comment.getCity());
        commentVo.setCreateTime(comment.getCreateTime());
        // 设置用户信息
        SysUserService userService = SpringUtils.getBean(SysUserService.class);
        SysUser user = userService.selectSysUserByUserId(comment.getUserId());
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setUsername(user.getUserName());
        authorInfo.setNickname(user.getNickName());
        authorInfo.setAvatarurl(user.getAvatar());
        // TODO 2024/5/11/16:56 以后会有一个 个人主页表  这些信息都是由这个表收集
        authorInfo.setAuthorHome("https://www.zhihu.com/people/www.zhiyuan.com");
        authorInfo.setSignature("我的个性签名" + RandomUtil.randomString(10));
        commentVo.setAuthorInfo(authorInfo);
        return commentVo;
    }

    /**
     * 导出数据 Excel格式
     */
    @Operation(summary = "导出数据 Excel格式")
    @GetMapping("/export")
    public void export(HttpServletResponse response, Comment comment) {
        // 忽略字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("updateBy");
        excludeColumnFiledNames.add("updateTime");
        excludeColumnFiledNames.add("remark");
        excludeColumnFiledNames.add("isDelete");
        excludeColumnFiledNames.add("params");
        List<Comment> list = commentService.selectCommentList(comment);
        ExcelUtil<Comment> util = new ExcelUtil<>(Comment.class);
        util.exportExcel(response, list, "评论数据", excludeColumnFiledNames);
    }


    /**
     * 获取 评论 详细信息
     */
    @Operation(summary = "根据ID获取详细信息")
    @GetMapping("/{commentId}")
    public R<Comment> getInfo(
            @Parameter(name = "commentId", description = "ID", in = ParameterIn.PATH)
            @PathVariable("commentId") Long commentId) {
        return R.success(commentService.selectCommentByCommentId(commentId));
    }


    /**
     * 新增 评论
     */
    @Operation(summary = "添加评论")
    @PostMapping("/reply")
    public R<Integer> add(@RequestBody @Valid CommentParams params, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new UserNotExistsException();
        }
        commentService.insert(params, user, request);
        return R.success();
    }


    /**
     * 修改 评论
     */
    @Operation(summary = "修改")
    @PutMapping("/update")
    public R<Integer> update(@RequestBody Comment comment) {
        return R.success(commentService.update(comment));
    }


    /**
     * 删除 评论
     * /delete/1,2,3
     */
    @Operation(summary = "删除")
    @DeleteMapping("/delete/{commentIds}")
    public R<Integer> delete(@PathVariable("commentIds") Long[] commentIds) {
        return R.success(commentService.delete(commentIds));
    }


}
