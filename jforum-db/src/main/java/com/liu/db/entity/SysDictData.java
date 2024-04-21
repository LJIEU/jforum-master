package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 字典数据对象 sys_dict_data
 *
 * @author JIE
 * @since 2024-04-11
 */
@Schema(name = "字典数据--实体类")
public class SysDictData extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 字典编码
     */
    @Schema(description = "字典编码")
    @ExcelProperty(value = "字典编码")
    private Long dictCode;
    /**
     * 字典标签
     */
    @Schema(description = "字典标签")
    @ExcelProperty(value = "字典标签")
    private String dictLabel;
    /**
     * 字典键值
     */
    @Schema(description = "字典键值")
    @ExcelProperty(value = "字典键值")
    private String dictValue;
    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    @ExcelProperty(value = "字典类型")
    private String dictType;
    /**
     * 样式属性
     */
    @Schema(description = "样式属性")
    @ExcelProperty(value = "样式属性")
    private String cssClass;
    /**
     * 表格回显样式
     */
    @Schema(description = "表格回显样式")
    @ExcelProperty(value = "表格回显样式")
    private String listClass;
    /**
     * 是否默认(Y是 N否)
     */
    @Schema(description = "是否默认(Y是 N否)")
    @ExcelProperty(value = "是否默认(Y是 N否)")
    private String isDefault;
    /**
     * 排序
     */
    @Schema(description = "排序")
    @ExcelProperty(value = "排序")
    private Integer sort;
    /**
     * 状态(0正常 1停用)
     */
    @Schema(description = "状态(0正常 1停用)")
    @ExcelProperty(value = "状态(0正常 1停用)")
    private String status;


    public void setDictCode(Long dictCode) {
        this.dictCode = dictCode;
    }

    public Long getDictCode() {
        return dictCode;
    }


    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel() {
        return dictLabel;
    }


    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictValue() {
        return dictValue;
    }


    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictType() {
        return dictType;
    }


    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }


    public void setListClass(String listClass) {
        this.listClass = listClass;
    }

    public String getListClass() {
        return listClass;
    }


    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefault() {
        return isDefault;
    }


    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dictCode", getDictCode())
                .append("dictLabel", getDictLabel())
                .append("dictValue", getDictValue())
                .append("dictType", getDictType())
                .append("cssClass", getCssClass())
                .append("listClass", getListClass())
                .append("isDefault", getIsDefault())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("isDelete", getIsDelete())
                .append("remark", getRemark())
                .toString();
    }
}