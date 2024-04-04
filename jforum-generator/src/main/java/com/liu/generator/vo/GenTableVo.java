package com.liu.generator.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 8:15
 */
@Data
@Schema(name = "导入表选项")
public class GenTableVo {
    @Schema(description = "表功能描述")
    private String functionName;

    @NotBlank(message = "表名称不能为空")
    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "模板选择[curd[默认],tree,sub]")
    private String template;

    @Schema(description = "关联表名称")
    private String subName;

    @Schema(description = "关联表主键")
    private String subPk;
}
