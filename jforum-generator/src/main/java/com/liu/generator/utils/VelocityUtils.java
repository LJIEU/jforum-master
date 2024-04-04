package com.liu.generator.utils;

import com.liu.core.model.BaseEntity;
import com.liu.generator.GenProperties;
import com.liu.generator.entity.GenTable;
import com.liu.generator.entity.GenTableColumn;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 模板引擎工具
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 20:07
 */
public class VelocityUtils {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VelocityUtils.class);

    /**
     * 设置模板变量信息
     *
     * @param genTable      需要生成的表 其信息记录
     * @param genProperties 配置类
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTable genTable, GenProperties genProperties) {
        String tableName = genTable.getTableName();
        List<GenTableColumn> columns = genTable.getColumns();
        HashSet<String> importList = new HashSet<>();
        // 获取表中不可以为NUll的字段 这些可以用于查询
        List<GenTableColumn> selectColumns = new ArrayList<>();
        for (GenTableColumn column : columns) {
            // 如果 该字段 在 BaseEntity 中有 就跳过后面的操作 以及对 Java字段的赋值
            String javaField = column.getJavaField();
            if (getBaseFields().contains(javaField)) {
                // 标记该字段
                column.setBaseHave("true");
            }
            // GoodsId
            column.setCapJavaField(Character.toUpperCase(javaField.charAt(0)) + javaField.substring(1));
            // 添加 需要导入的类
            String columnType = column.getColumnType();
            if (Arrays.asList("datetime" , "time").contains(columnType)) {
                // 时间类型
                importList.add("java.util.Date");
                // 需要设置时间格式
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            } else if (Stream.of("tinyint" , "bigint" , "decimal" , "number").anyMatch(columnType::contains)) {
                // 如果是浮点型 统一使用 BigDecimal
                if (columnType.contains(",")) {
                    importList.add("java.math.BigDecimal");
                }
            }
            // 设置哪些可以查询 哪些不可以查询
            if (Objects.equals(column.getIsRequired(), "1") &&
                    !Arrays.asList("is_delete" , "remark" , "note" , "password").contains(column.getColumnName())) {
                selectColumns.add(column);
            }
        }

        return getContext(genTable, genProperties, tableName, columns, importList, selectColumns);
    }

    private static VelocityContext getContext(GenTable genTable, GenProperties genProperties, String tableName, List<GenTableColumn> columns, HashSet<String> importList, List<GenTableColumn> selectColumns) {
        // 设置模板变量信息
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName" , genProperties.getPackageName() + "." + genProperties.getModuleName());
        velocityContext.put("importList" , importList);
        velocityContext.put("tableDesc" , getTableDesc(genTable.getTableComment()));
        velocityContext.put("tableName" , tableName);
        velocityContext.put("template" , genTable.getTplCategory());
        velocityContext.put("author" , genProperties.getAuthor());
        velocityContext.put("functionName" , genTable.getFunctionName());
        velocityContext.put("dateTime" , new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        velocityContext.put("ClassName" , getClassName(tableName, genProperties));
        velocityContext.put("baseEntity" , "BaseEntity");
        velocityContext.put("columns" , columns);
        /*与基类缺少的部分*/
        velocityContext.put("lackColumns" , getLackColumns(columns));
        GenTableColumn pkColumn = columns.stream().filter(v -> Objects.equals(v.getIsPk(), "1"))
                .findFirst().orElse(null);
        velocityContext.put("pkColumn" , pkColumn);
        velocityContext.put("selectColumns" , selectColumns);
        velocityContext.put("sqlColumns" , columns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList()));
        // 建立 关联表
        if (genTable.getTplCategory().equalsIgnoreCase("sub")) {
            // 当前表的pk 和 关联表的pk
            String subTableName = genTable.getSubTableName();
            String subPkName = genTable.getSubTableFkName();
            // 整理关联表的 表名
            String relationName = getRelation(tableName, subTableName);
            // 表描述
            String relationDesc = genTable.getTableComment().concat(" 与 " + subTableName + " 关联表");
            velocityContext.put("relationName" , relationName);
            velocityContext.put("subPkName" , subPkName);
            velocityContext.put("relationDesc" , relationDesc);
        }
        return velocityContext;
    }


    /**
     * 获取关联表的名称
     *
     * @param tableName    主表
     * @param subTableName 从表
     */
    private static String getRelation(String tableName, String subTableName) {
        // 1. test_one  + test_two ==> test_one_two
        // 2. user + sys_role ==>  user_role
        // 3. gen_user + test_role ==> gen_user_role
        // 4. gen_user_abc + test_role_abc ==> user_role
        String[] splitSubTableName = subTableName.split("_");
        return tableName.concat("_" + splitSubTableName[splitSubTableName.length - 1]);
    }

    /**
     * 获取基类的 属性列表
     */
    public static List<String> getBaseFields() {
        return Arrays.stream(BaseEntity.class.getDeclaredFields()).map(Field::getName)
                .filter(v -> !v.equals("serialVersionUID"))
                .collect(Collectors.toList());
    }

    /**
     * 获取 表 在 基类中没有的字段  如有的表不存在 isDelete
     */
    private static List<String> getLackColumns(List<GenTableColumn> columns) {
        ArrayList<String> lackColumns = new ArrayList<>();
        // 先获取基类的所有属性private、public等 getDeclaredFields() ==> 不会去获取其父类的属性
        Field[] declaredFields = BaseEntity.class.getDeclaredFields();
        List<String> fieldName = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toList());
        List<String> originalColumns = columns.stream().map(GenTableColumn::getJavaField).collect(Collectors.toList());
        for (String baseName : fieldName) {
            // 如果当前表的字段中 未含有该基类字段则记录
            if (!originalColumns.contains(baseName) && !baseName.equals("serialVersionUID")) {
                lackColumns.add(baseName);
            }
        }
        return lackColumns;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList() {
        List<String> templates = new ArrayList<String>();
        // 获取模板列表
        templates.add("vm/java/dao.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        return templates;
    }

    /**
     * 描述
     */
    private static String getTableDesc(String tableComment) {
        int index = tableComment.indexOf("表");
        if (index != -1) {
            return tableComment.substring(0, index);
        }
        return tableComment;

    }


    /**
     * 获取类名 Goods
     */
    public static String getClassName(String tableName, GenProperties genProperties) {
        // 这里需要特殊处理 看是否需要前缀 如: t_goods => TGoods | Goods
        String tablePrefix = genProperties.getTablePrefix();
        String[] split = tablePrefix.split("[,，]");
        boolean autoRemovePre = genProperties.isAutoRemovePre();
        if (autoRemovePre) {
            // 进行去除操作  Goods
            for (String s : split) {
                // 前缀长度
                int length = s.length();
                if (tableName.substring(0, length).equals(s)) {
                    // 处理表前缀
                    tableName = tableName.substring(s.length());
                    break;
                }
            }
        }
        // TGoods
        String[] s = tableName.split("_");
        StringBuilder sb = new StringBuilder(tableName.length());
        for (String str : s) {
            sb.append(StringUtils.capitalize(str));
        }
        return sb.toString();
    }

    /**
     * 获取文件名 com.liu....
     */
    public static String getFileName(String template, GenTable genTable, GenProperties genProperties) {
        // 文件名
        String fileName = "";
        // 包路径
        String packageName = genProperties.getPackageName();
        // 模块名
        String moduleName = genProperties.getModuleName();
        // 类名
        String className = getClassName(genTable.getTableName(), genProperties);

        String javaPath = "main/java/" + StringUtils.replace(packageName, "." , "/")
                + "/" + moduleName;
        String mybatisPath = "main/resources/mapper/" + moduleName;

        if (template.contains("dao.java.vm")) {
            fileName = javaPath + "/dao/" + className + ".java";
        } else if (template.contains("mapper.java.vm")) {
            fileName = javaPath + "/mapper/" + className + "Mapper.java";
        } else if (template.contains("service.java.vm")) {
            fileName = javaPath + "/service/" + className + "Service.java";
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = javaPath + "/service/impl/" + className + "ServiceImpl.java";
        } else if (template.contains("controller.java.vm")) {
            fileName = javaPath + "/controller/" + className + "Controller.java";
        } else if (template.contains("mapper.xml.vm")) {
            fileName = mybatisPath + "/" + className + "Mapper.xml";
        } else if (template.contains("sql.sql.vm") && genTable.getTplCategory().equalsIgnoreCase("sub")) {
            fileName = "/" + className + ".sql";
        }
        log.warn(fileName);
        return fileName;
    }
}
