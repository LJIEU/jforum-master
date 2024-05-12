package com.liu.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.liu.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;

/**
 * 点赞对象 like
 *
 * @author JIE
 * @since 2024-05-11
 */
@Schema(name = "点赞--实体类")
public class Like extends BaseEntity{
@Serial
private static final long serialVersionUID=1L;
        /** 点赞ID */
    @Schema(description = "点赞ID")
    @ExcelProperty(value = "点赞ID")
    private Long likeId;
        /** 点赞类型 comment评论  post帖子 */
    @Schema(description = "点赞类型 comment评论  post帖子")
    @ExcelProperty(value = "点赞类型 comment评论  post帖子")
    private String type;
        /** 类型ID 评论ID 或者 帖子ID */
    @Schema(description = "类型ID 评论ID 或者 帖子ID")
    @ExcelProperty(value = "类型ID 评论ID 或者 帖子ID")
    private String typeId;
        


            
            
    public void setLikeId(Long likeId){
            this.likeId = likeId;
            }
    public Long getLikeId(){
            return likeId;
            }
            
            
    public void setType(String type){
            this.type = type;
            }
    public String getType(){
            return type;
            }
            
            
    public void setTypeId(String typeId){
            this.typeId = typeId;
            }
    public String getTypeId(){
            return typeId;
            }
        
@Override
public String toString(){
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("likeId",getLikeId())
            .append("type",getType())
            .append("typeId",getTypeId())
            .append("createBy",getCreateBy())
            .append("createTime",getCreateTime())
        .toString();
        }
        }