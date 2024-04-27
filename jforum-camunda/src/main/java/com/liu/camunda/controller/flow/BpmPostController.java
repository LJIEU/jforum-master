package com.liu.camunda.controller.flow;

import com.liu.camunda.service.BpmPostService;
import com.liu.camunda.vo.BpmPostVo;
import com.liu.core.excption.ServiceException;
import com.liu.core.result.R;
import com.liu.core.utils.SecurityUtils;
import com.liu.core.utils.SpringUtils;
import com.liu.db.entity.SysUser;
import com.liu.db.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description: 帖子审批流程
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/26 13:33
 */
@Tag(name = "帖子审批")
@RequestMapping("/my_camunda/post")
@RestController
public class BpmPostController {

    @Autowired
    private BpmPostService bpmPostService;

    @Operation(summary = "流程启动")
    @PostMapping("/start")
    public R<Map<String, Object>> startPostFlow(@RequestBody BpmPostVo bpmPostVo, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("当前用户不存在!");
        }
        return bpmPostService.startPostFlow(bpmPostVo, user);
    }

    @Operation(summary = "发起审核")
    @PostMapping("/initiateReview")
    public R<String> initiateReview(
            @Parameter(name = "processInstanceId", description = "流程实例ID", in = ParameterIn.QUERY)
            @RequestParam("processInstanceId") String processInstanceId) {
        return bpmPostService.initiateReview(processInstanceId);
    }

    @Operation(summary = "删除流程实例")
    @PostMapping("/delete")
    public R<String> delete(
            @Parameter(name = "processInstanceId", description = "流程实例ID", in = ParameterIn.QUERY)
            @RequestParam("processInstanceId") String processInstanceId) {
        return bpmPostService.delete(processInstanceId);
    }

    @Operation(summary = "进行审核")
    @PostMapping("/reviewing")
    public R<String> reviewing(
            @Parameter(name = "processInstanceId", description = "流程实例ID", in = ParameterIn.QUERY)
            @RequestParam("processInstanceId") String processInstanceId, HttpServletRequest request) {
        SysUser user = SpringUtils.getBean(SysUserService.class).getItemByUserName(SecurityUtils.currentUsername(request));
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        return bpmPostService.reviewing(processInstanceId, user);
    }



}
