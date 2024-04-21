package com.liu.generator.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.liu.core.constant.Constants;
import com.liu.generator.GenProperties;
import com.liu.generator.entity.GenTable;
import com.liu.generator.entity.GenTableColumn;
import com.liu.generator.mapper.GenTableColumnMapper;
import com.liu.generator.mapper.GenTableMapper;
import com.liu.generator.utils.VelocityInitializer;
import com.liu.generator.utils.VelocityUtils;
import com.liu.generator.vo.GenTableVo;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/03/30 17:20
 */
@Service
public class GenTableService {
    public static final Logger log = LoggerFactory.getLogger(GenTableService.class);


    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    @Autowired
    private GenProperties genProperties;

    /**
     * 将表数据对应的导入
     *
     * @param genTableVo 导入表选项
     * @return 返回表信息
     */
    @Transactional(rollbackFor = Exception.class)
    public GenTable insertInfo(GenTableVo genTableVo) {
        String tableName = genTableVo.getTableName();
        String functionName = genTableVo.getFunctionName();
        GenTable tableInfo = genTableMapper.getTableInfo(tableName);
        if (ObjectUtil.isEmpty(tableInfo)) {
            throw new RuntimeException("表不存在");
        }
        // 设置类名 包  所属模块 作者 表功能
        tableInfo.setClassName(VelocityUtils.getClassName(tableName, genProperties));
        tableInfo.setPackageName(genProperties.getPackageName());
        tableInfo.setModuleName(genProperties.getModuleName());
        tableInfo.setFunctionAuthor(genProperties.getAuthor());
        tableInfo.setFunctionName(StrUtil.isBlank(functionName) ? null : functionName);
        // 设置 分类 以及 关联表信息 采用默认
        String template = genTableVo.getTemplate();
        try {
            if (!List.of(new String[]{"crud", "tree", "sub"}).contains(template)) {
                template = "crud";
            }
        } catch (Exception e) {
            template = "crud";
        }
        if (template.equals("sub")) {
            String subName = genTableVo.getSubName();
            String subPk = genTableVo.getSubPk();
            // 查看关联表是否存在
            String subTableName = genTableMapper.selectTableByName(subName);
            if (StrUtil.isEmpty(subTableName)) {
                throw new RuntimeException("关联表不存在,请检查输入是否正确~");
            }
            // 查询主键
            GenTableColumn pkColumn = genTableColumnMapper.selectPkByTableName(subName);
            subPk = pkColumn.getColumnName();

            tableInfo.setTplCategory(template);
            tableInfo.setSubTableName(subTableName);
            tableInfo.setSubTableFkName(subPk);
        } else if (template.equals("tree")) {
            tableInfo.setTplCategory(template);
        }

        // 插入并返回ID
        Long id = genTableMapper.insertGenTable(tableInfo);
        // 根据 Unix 时间戳
//        Long id = genTableMapper.selectId(tableInfo.getCreateTime().getTime() / 1000L);
        if (tableInfo.getTableId() == null) {
            tableInfo.setTableId(id);
        }
        return tableInfo;
    }

    /**
     * 预览生成代码
     *
     * @param ids id列表
     * @return 返回结果
     */
    public Map<String, String> previewCode(String[] ids) {
        Map<String, String> dataMap = new HashMap<>();
        for (String id : ids) {
            // 初始化 Velocity引擎
            VelocityInitializer.initVelocity();
            // 获取表信息
            GenTable genTable = genTableMapper.selectTableById(id);
            if (genTable == null) {
                return dataMap;
            }
            // 获取表的字段和对应的Java类型以及字段描述
            genTable.setColumns(genTableColumnMapper.getColumnsByTableId(genTable.getTableId()));

            // 配置模板变量信息
            VelocityContext context = VelocityUtils.prepareContext(genTable, genProperties);

            // 获取模板路径
            List<String> templates = getTemplateList(genTable);

            for (String template : templates) {
                // 开始渲染
                StringWriter writer = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                // 将模板变量属性写入
                tpl.merge(context, writer);
                dataMap.put(genTable.getTableName() + ":" + template, writer.toString());
            }
        }
        return dataMap;
    }

    private List<String> getTemplateList(GenTable genTable) {
        List<String> templates = VelocityUtils.getTemplateList();
        if (genTable.getTplCategory().equalsIgnoreCase("sub")) {
            templates.add("vm/sql/sql.sql.vm");
        }
        return templates;
    }

    /**
     * 下载代码
     *
     * @param ids 待代码生成的ID列表
     */
    public byte[] downloadCode(String[] ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String id : ids) {
            if (!id.matches("[0-9]+")) {
                throw new RuntimeException("非法参数!");
            }
            // 初始化 Velocity引擎
            VelocityInitializer.initVelocity();
            GenTable genTable = genTableMapper.selectTableById(id);
            genTable.setColumns(genTableColumnMapper.getColumnsByTableId(genTable.getTableId()));
            VelocityContext context = VelocityUtils.prepareContext(genTable, genProperties);
            List<String> templates = getTemplateList(genTable);
            for (String template : templates) {
                // 开始渲染
                StringWriter writer = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                // 将模板变量属性写入
                tpl.merge(context, writer);
                try {
                    // 添加到 zip 中
                    zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, genTable, genProperties)));
                    IOUtils.write(writer.toString(), zip, StandardCharsets.UTF_8);
                    IOUtils.closeQuietly(writer);
                    zip.flush();
                    zip.closeEntry();
                } catch (IOException e) {
                    log.error("渲染失败,表名:{} --> {}", genTable.getTableName(), e);
                }
            }
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
