package com.liu.system.dao;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 测试对象 test_one
 *
 * @author JIE
 * @since 2024-03-31
 */
@Schema(name = "测试--实体类")
public class TestOne extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @Schema(description = "ID")
//    @NotNull(message = "ID错误!不能为Null")
    private Long oneId;
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @ExcelProperty(value = "姓名")
    @Length(max = 20, message = "姓名长度不能大于20位")
    @NotBlank(message = "字符串不能为空!")
    private String oneName;
    /**
     * 年龄
     */
    @Schema(description = "年龄")
    @ExcelProperty(value = "年龄")
    @Max(value = 150, message = "年龄已达上限")
    @Min(value = 1, message = "年龄已达下限")
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setOneId(Long oneId) {
        this.oneId = oneId;
    }

    public Long getOneId() {
        return oneId;
    }


    public void setOneName(String oneName) {
        this.oneName = oneName;
    }

    public String getOneName() {
        return oneName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("oneId", getOneId())
                .append("oneName", getOneName())
                .append("age", getAge())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}