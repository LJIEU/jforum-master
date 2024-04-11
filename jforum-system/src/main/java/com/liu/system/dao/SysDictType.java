package com.liu.system.dao;

    import java.util.Date;
    import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.liu.core.model.BaseEntity;

import java.io.Serial;

/**
 * 字典类型对象 sys_dict_type
 *
 * @author JIE
 * @since 2024-04-11
 */
@Schema(name = "字典类型--实体类")
public class SysDictType extends BaseEntity{
@Serial
private static final long serialVersionUID=1L;
        /** 字典ID */
    @Schema(description = "字典ID")
    @ExcelProperty(value = "字典ID")
    private Long dictId;
        /** 字典名称 */
    @Schema(description = "字典名称")
    @ExcelProperty(value = "字典名称")
    private String dictName;
        /** 字典类型 */
    @Schema(description = "字典类型")
    @ExcelProperty(value = "字典类型")
    private String dictType;
        /** 状态(0正常 1停用) */
    @Schema(description = "状态(0正常 1停用)")
    @ExcelProperty(value = "状态(0正常 1停用)")
    private String status;
                    


            
            
    public void setDictId(Long dictId){
            this.dictId = dictId;
            }
    public Long getDictId(){
            return dictId;
            }
            
            
    public void setDictName(String dictName){
            this.dictName = dictName;
            }
    public String getDictName(){
            return dictName;
            }
            
            
    public void setDictType(String dictType){
            this.dictType = dictType;
            }
    public String getDictType(){
            return dictType;
            }
            
            
    public void setStatus(String status){
            this.status = status;
            }
    public String getStatus(){
            return status;
            }
                    
@Override
public String toString(){
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId",getDictId())
            .append("dictName",getDictName())
            .append("dictType",getDictType())
            .append("status",getStatus())
            .append("createBy",getCreateBy())
            .append("createTime",getCreateTime())
            .append("updateBy",getUpdateBy())
            .append("updateTime",getUpdateTime())
            .append("remark",getRemark())
        .toString();
        }
        }