package com.liu.core.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.liu.core.converter.IsDeleteConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Description: 基类 --》 实体类
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 17:07
 */
@HeadFontStyle(fontHeightInPoints = 12, bold = BooleanEnum.TRUE)
@HeadRowHeight(value = -1)
@HeadStyle(shrinkToFit = BooleanEnum.TRUE,
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        // 使用 换行符
        wrapped = BooleanEnum.TRUE
)
public class BaseEntity implements Serializable {
    /**
     * 数据导出时 需要查看表中是否含右 基类这些字段 如果有没有的则进行 排除 避免导出空数据
     * 插入数据时 更新者和更新时间 不需要填写 <br>
     * 更新数据时 创建者和创建时间 不需要填写 <br>
     * 更新数据时 ID不可修改 所以不开放这个修改选项 <br>
     * 删除数据时 如果该表中存在字段is_delete就执行逻辑删除[修改] ==》 否则直接删除
     */
    @Serial
    private static final long serialVersionUID = -4432333345877682860L;

    /**
     * 搜索值
     */
//    @JsonIgnore
//    private String searchValue;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    @Schema(description = "创建者")
    private String createBy;


    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @Schema(description = "创建时间")
    @ColumnWidth(20) // 设置Excel表格中该属性为 20 像素宽度
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
//    @Future(message = "日期不在将来")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    @Schema(description = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    @ColumnWidth(20)
//    @Past(message = "日期不在将来")
    private Date updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @ExcelProperty(value = "备注")
    @Length(max = 500, message = "备注过长")
    private String remark;

    /**
     * 是否删除
     */
    @Schema(description = "逻辑删除")
    @ExcelProperty(value = "删除", converter = IsDeleteConverter.class)
    private Integer isDelete;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ExcelIgnore // 导出时忽略此字段
    private Map<String, Object> params;


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
