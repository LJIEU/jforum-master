package com.liu.db.nodb.document;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 11:06
 */

@Document(collection = "tag")
public class TagDocument extends BaseDocument {
    @Serial
    private static final long serialVersionUID = -5464995691050899404L;

    @Schema(description = "标签名")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
