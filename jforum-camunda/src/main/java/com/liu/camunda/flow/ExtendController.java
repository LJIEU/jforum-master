package com.liu.camunda.flow;

import com.liu.camunda.service.ExtendService;
import com.liu.camunda.vo.SequentialAddVo;
import com.liu.core.config.dynamic.DataSource;
import com.liu.core.constant.enume.DataSourceType;
import com.liu.core.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * 扩展功能
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/19 21:11
 */
@Tag(name = "扩展功能")
@RequestMapping("/my_camunda/extend")
@RequiredArgsConstructor
@RestController
public class ExtendController {
    private final ExtendService extendService;


    /**
     * 依次审批加签
     *
     * @param requestParam 请求参数
     * @return String
     */
    @Operation(summary = "审批批准")
    @PostMapping("/addAssignee")
    @DataSource(DataSourceType.CAMUNDA)
    public R<String> addAssignee(@RequestBody @Validated SequentialAddVo requestParam) {
        return extendService.addAssignee(requestParam);
    }
}
