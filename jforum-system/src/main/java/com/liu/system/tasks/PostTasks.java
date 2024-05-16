package com.liu.system.tasks;

import com.liu.camunda.constants.BpmnConstants;
import com.liu.camunda.service.ProcessInstanceService;
import com.liu.core.constant.PostState;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.Post;
import com.liu.db.entity.SysUser;
import com.liu.db.service.PostService;
import com.liu.db.service.SysUserService;
import jakarta.annotation.Resource;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 帖子审核流程发起任务
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 19:47
 */
@Component
public class PostTasks {
    private final Logger log = LoggerFactory.getLogger(PostTasks.class);

    @Resource
    private RuntimeService runtimeService;

    @Autowired
    private PostService postService;

    @Autowired
    private ProcessInstanceService processInstanceService;

    // 每5s执行一次
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        // 根据部署ID查询流程实例条数
        long count = runtimeService.createProcessInstanceQuery().deploymentId("8af89803-0a9f-11ef-8e0b-005056c00001").active().count();
        log.warn("查询已拥有流程条数{}", count);
        // 如果少于20条，则启动流程
        if (count < 20) {
            // 查询 20条 待审核的帖子ID
            List<Post> posts = postService.pendingPostList(PostState.POST_PENDING);
//            posts.forEach(System.out::println);
            for (Post post : posts) {
                SysUser user = SpringUtils.getBean(SysUserService.class).selectSysUserByUserId(post.getUserId());
                Map<String, Object> params = new HashMap<>(1);
                params.put(BpmnConstants.POST_ID, post.getPostId());
                // 开启审核流程
                processInstanceService.startProcessInstanceByDeployId("8af89803-0a9f-11ef-8e0b-005056c00001", params, user);
                // 如果没什么异常 修改 帖子状态为3
                post.setState(PostState.POST_REVIEWING);
                postService.update(post);
            }
        }
    }
}
