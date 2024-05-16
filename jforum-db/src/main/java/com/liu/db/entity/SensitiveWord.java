package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 敏感词对象 sensitive_word
 *
 * @author JIE
 * @since 2024-05-16
 */
@Schema(name = "敏感词--实体类")
public class SensitiveWord extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @Schema(description = "")
    @ExcelProperty(value = "")
    private Long id;
    /**
     * 敏感词内容
     */
    @Schema(description = "敏感词内容")
    @ExcelProperty(value = "敏感词内容")
    private String word;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("word", getWord())
                .toString();
    }
}