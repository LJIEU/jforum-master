package com.liu.db.entity;

    import com.alibaba.excel.annotation.ExcelProperty;
    import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.liu.core.model.BaseEntity;

import java.io.Serial;

/**
 * 操作日志对象 sys_operate_log
 *
 * @author JIE
 * @since 2024-04-03
 */
@Schema(name = "操作日志--实体类")
public class SysOperateLog extends BaseEntity{
@Serial
private static final long serialVersionUID=1L;
        /** 操作ID */
    @Schema(description = "操作ID")
    @ExcelProperty(value = "操作ID")
    private Long operateId;
        /** 操作人员 */
    @Schema(description = "操作人员")
    @ExcelProperty(value = "操作人员")
    private String username;
        /** 业务类型（0其它 1新增 2修改 3删除） */
    @Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
    @ExcelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;
        /** 操作类别（0其它 1后台用户 2手机端用户） */
    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    @ExcelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;
        /** IP地址 */
    @Schema(description = "IP地址")
    @ExcelProperty(value = "IP地址")
    private String ip;
        /** 远程地址 */
    @Schema(description = "远程地址")
    @ExcelProperty(value = "远程地址")
    private String operateLocation;
        /** 所属模块:sys,system,gen.... */
    @Schema(description = "所属模块:sys,system,gen....")
    @ExcelProperty(value = "所属模块:sys,system,gen....")
    private String moduleName;
        /** 类名 */
    @Schema(description = "类名")
    @ExcelProperty(value = "类名")
    private String className;
        /** 方法名 */
    @Schema(description = "方法名")
    @ExcelProperty(value = "方法名")
    private String methodName;
        /** 描述 */
    @Schema(description = "描述")
    @ExcelProperty(value = "描述")
    private String describe;
        /** 请求方式 */
    @Schema(description = "请求方式")
    @ExcelProperty(value = "请求方式")
    private String requestMethod;
        /** 请求链接 */
    @Schema(description = "请求链接")
    @ExcelProperty(value = "请求链接")
    private String requestUrl;
        /** 请求参数 */
    @Schema(description = "请求参数")
    @ExcelProperty(value = "请求参数")
    private String operateParam;
        /** 返回参数 */
    @Schema(description = "返回参数")
    @ExcelProperty(value = "返回参数")
    private String jsonResult;
        /** 执行时间 */
    @Schema(description = "执行时间")
    @ExcelProperty(value = "执行时间")
    private Long costTime;
        /** 操作状态: 0 正常  1异常 */
    @Schema(description = "操作状态: 0 正常  1异常")
    @ExcelProperty(value = "操作状态: 0 正常  1异常")
    private Integer status;
        /** 错误信息 */
    @Schema(description = "错误信息")
    @ExcelProperty(value = "错误信息")
    private String errorMessage;
                


            
            
    public void setOperateId(Long operateId){
            this.operateId = operateId;
            }
    public Long getOperateId(){
            return operateId;
            }
            
            
    public void setUsername(String username){
            this.username = username;
            }
    public String getUsername(){
            return username;
            }
            
            
    public void setBusinessType(Integer businessType){
            this.businessType = businessType;
            }
    public Integer getBusinessType(){
            return businessType;
            }
            
            
    public void setOperatorType(Integer operatorType){
            this.operatorType = operatorType;
            }
    public Integer getOperatorType(){
            return operatorType;
            }
            
            
    public void setIp(String ip){
            this.ip = ip;
            }
    public String getIp(){
            return ip;
            }
            
            
    public void setOperateLocation(String operateLocation){
            this.operateLocation = operateLocation;
            }
    public String getOperateLocation(){
            return operateLocation;
            }
            
            
    public void setModuleName(String moduleName){
            this.moduleName = moduleName;
            }
    public String getModuleName(){
            return moduleName;
            }
            
            
    public void setClassName(String className){
            this.className = className;
            }
    public String getClassName(){
            return className;
            }
            
            
    public void setMethodName(String methodName){
            this.methodName = methodName;
            }
    public String getMethodName(){
            return methodName;
            }
            
            
    public void setDescribe(String describe){
            this.describe = describe;
            }
    public String getDescribe(){
            return describe;
            }
            
            
    public void setRequestMethod(String requestMethod){
            this.requestMethod = requestMethod;
            }
    public String getRequestMethod(){
            return requestMethod;
            }
            
            
    public void setRequestUrl(String requestUrl){
            this.requestUrl = requestUrl;
            }
    public String getRequestUrl(){
            return requestUrl;
            }
            
            
    public void setOperateParam(String operateParam){
            this.operateParam = operateParam;
            }
    public String getOperateParam(){
            return operateParam;
            }
            
            
    public void setJsonResult(String jsonResult){
            this.jsonResult = jsonResult;
            }
    public String getJsonResult(){
            return jsonResult;
            }
            
            
    public void setCostTime(Long costTime){
            this.costTime = costTime;
            }
    public Long getCostTime(){
            return costTime;
            }
            
            
    public void setStatus(Integer status){
            this.status = status;
            }
    public Integer getStatus(){
            return status;
            }
            
            
    public void setErrorMessage(String errorMessage){
            this.errorMessage = errorMessage;
            }
    public String getErrorMessage(){
            return errorMessage;
            }
                
@Override
public String toString(){
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("operateId",getOperateId())
            .append("username",getUsername())
            .append("businessType",getBusinessType())
            .append("operatorType",getOperatorType())
            .append("ip",getIp())
            .append("operateLocation",getOperateLocation())
            .append("moduleName",getModuleName())
            .append("className",getClassName())
            .append("methodName",getMethodName())
            .append("describe",getDescribe())
            .append("requestMethod",getRequestMethod())
            .append("requestUrl",getRequestUrl())
            .append("operateParam",getOperateParam())
            .append("jsonResult",getJsonResult())
            .append("costTime",getCostTime())
            .append("status",getStatus())
            .append("errorMessage",getErrorMessage())
            .append("isDelete",getIsDelete())
            .append("createTime",getCreateTime())
            .append("createBy",getCreateBy())
            .append("remark",getRemark())
        .toString();
        }
        }