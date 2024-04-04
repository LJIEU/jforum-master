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
 * 参数配置对象 sys_config
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "参数配置--实体类")
public class SysConfig extends BaseEntity{
@Serial
private static final long serialVersionUID=1L;
        /** 参数主键 */
    @Schema(description = "参数主键")
    @ExcelProperty(value = "参数主键")
    private Integer configId;
        /** 参数名称 */
    @Schema(description = "参数名称")
    @ExcelProperty(value = "参数名称")
    private String configName;
        /** 参数键名 */
    @Schema(description = "参数键名")
    @ExcelProperty(value = "参数键名")
    private String configKey;
        /** 参数键值 */
    @Schema(description = "参数键值")
    @ExcelProperty(value = "参数键值")
    private String configValue;
        /** 系统内置（Y是 N否） */
    @Schema(description = "系统内置（Y是 N否）")
    @ExcelProperty(value = "系统内置（Y是 N否）")
    private String configType;
                        


            
            
    public void setConfigId(Integer configId){
            this.configId = configId;
            }
    public Integer getConfigId(){
            return configId;
            }
            
            
    public void setConfigName(String configName){
            this.configName = configName;
            }
    public String getConfigName(){
            return configName;
            }
            
            
    public void setConfigKey(String configKey){
            this.configKey = configKey;
            }
    public String getConfigKey(){
            return configKey;
            }
            
            
    public void setConfigValue(String configValue){
            this.configValue = configValue;
            }
    public String getConfigValue(){
            return configValue;
            }
            
            
    public void setConfigType(String configType){
            this.configType = configType;
            }
    public String getConfigType(){
            return configType;
            }
                        
@Override
public String toString(){
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId",getConfigId())
            .append("configName",getConfigName())
            .append("configKey",getConfigKey())
            .append("configValue",getConfigValue())
            .append("configType",getConfigType())
            .append("createBy",getCreateBy())
            .append("createTime",getCreateTime())
            .append("updateBy",getUpdateBy())
            .append("updateTime",getUpdateTime())
            .append("isDelete",getIsDelete())
            .append("remark",getRemark())
        .toString();
        }
        }