package com.duomai.new_custom_base.api.product.gen.service;

import com.alibaba.fastjson.JSONObject;
import com.duomai.new_custom_base.api.product.gen.GenConstants;
import com.duomai.new_custom_base.api.product.gen.GenUtils;
import com.duomai.new_custom_base.api.product.gen.VelocityInitializer;
import com.duomai.new_custom_base.api.product.gen.VelocityUtils;
import com.duomai.new_custom_base.api.product.gen.domain.GenTable;
import com.duomai.new_custom_base.api.product.gen.domain.GenTableColumn;
import com.duomai.new_custom_base.api.product.gen.mapper.GenTableMapper;
import com.duomai.new_custom_base.framework.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 */
@Service
@Slf4j
public class GenTableServiceImpl extends BaseServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    @Override
    public List<GenTable> selectGenTableList(GenTable genTable) {
        return query().list();
    }

    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return baseMapper.selectDbTableList(genTable);
    }

    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames) {
        return baseMapper.selectDbTableListByNames(tableNames);
    }

    @Override
    public void importGenTable(List<GenTable> tableList) {
        for (GenTable table : tableList) {
            try {
                GenUtils.initTable(table);
                save(table);
            } catch (Exception e) {
                log.error("表名 " + table.getTableName() + " 导入失败：", e);
            }
        }
    }

    @Override
    public Map<String, String> previewCode(Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        GenTable table = baseMapper.selectById(tableId);
        List<GenTableColumn> genTableColumns = baseMapper.selectDbTableColumnsByName(table.getTableName());
        table.setColumns(genTableColumns);
        for (GenTableColumn column : genTableColumns) {
            GenUtils.initColumnField(column, table);
        }

        // 查询列信息
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList();
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    @Override
    public byte[] generatorCode(String[] tableIds) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableId : tableIds) {
            generatorCode(Long.valueOf(tableId), zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(Long tableId, ZipOutputStream zip) {
        GenTable table = baseMapper.selectById(tableId);
        List<GenTableColumn> genTableColumns = baseMapper.selectDbTableColumnsByName(table.getTableName());
        table.setColumns(genTableColumns);

        // 查询列信息
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList();
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table   业务表信息
     * @param columns 业务字段列表
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns) {
        for (GenTableColumn column : columns) {
            if (column.isPk()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (table.getPkColumn() == null) {
            table.setPkColumn(columns.get(0));
        }
    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (paramsObj != null) {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
        }
    }
}