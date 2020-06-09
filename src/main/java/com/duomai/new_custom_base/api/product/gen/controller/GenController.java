package com.duomai.new_custom_base.api.product.gen.controller;

import com.duomai.new_custom_base.api.product.gen.GenUtils;
import com.duomai.new_custom_base.api.product.gen.VelocityInitializer;
import com.duomai.new_custom_base.api.product.gen.VelocityUtils;
import com.duomai.new_custom_base.api.product.gen.entity.GenTable;
import com.duomai.new_custom_base.api.product.gen.entity.GenTableColumn;
import com.duomai.new_custom_base.api.product.gen.repository.GenTableRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-29 14:27
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController {

    @Autowired
    private GenTableRepository genTableRepository;


    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> list(GenTable genTable, Integer pageNum, Integer pageSize) {
        Page<GenTable> all = genTableRepository.findAll(PageRequest.of(pageNum - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", all.getTotalPages());
        result.put("rows", all.get().collect(Collectors.toList()));
        return result;
    }

    /**
     * 导入表结构
     */
    @GetMapping("/importTable")
    public String importTable() {
        return "gen/importTable";
    }

    @PostMapping("/importTable")
    @ResponseBody
    public void importTableSave(String tables) {
        // 查询表信息
        List<Object[]> objects = genTableRepository.selectDbTableListByNames(tables.split(","));
        if (objects != null)
            objects.forEach(obj -> {
                GenTable genTable = new GenTable();
                genTable.setTableName((String) obj[0]);
                genTable.setTableComment((String) obj[1]);
                GenUtils.initTable(genTable);
                genTableRepository.save(genTable);
            });
    }

    @PostMapping("/remove")
    @ResponseBody
    public void remveTable(String tableId) {
        if (StringUtils.isNotBlank(tableId))
            genTableRepository.deleteById(Long.valueOf(tableId));
    }

    @GetMapping("/db/list")
    @ResponseBody
    public Map<String, Object> dataList(String tableName, String tableComment, Integer pageNum, Integer pageSize) {

        Page<Object[]> all = genTableRepository.selectDbTableList(tableName, tableComment, PageRequest.of(pageNum - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", all.getTotalPages());
        result.put("rows", all.get().map((obj) -> {
            GenTable genTable = new GenTable();
            genTable.setTableName((String) obj[0]);
            genTable.setTableComment((String) obj[1]);
            return genTable;
        }).collect(Collectors.toList()));
        return result;
    }

    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public Map<String, String> preview(@PathVariable("tableId") Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        GenTable table = genTableRepository.findById(tableId).get();
        List<Object[]> genTableColumnObjects = genTableRepository.selectDbTableColumnsByName(table.getTableName());
        table.setColumns(genTableColumnObjects.stream().map(obj -> {
            GenTableColumn column = new GenTableColumn();
            column.setColumnName((String) obj[0]);
            column.setColumnComment((String) obj[1]);
            column.setColumnType((String) obj[2]);
            GenUtils.initColumnField(column, table);
            return column;
        }).collect(Collectors.toList()));
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

    @PostMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableId : tables.split(",")) {
            GenTable table = genTableRepository.findById(Long.valueOf(tableId)).get();
            List<Object[]> genTableColumnObjects = genTableRepository.selectDbTableColumnsByName(table.getTableName());
            table.setColumns(genTableColumnObjects.stream().map(obj -> {
                GenTableColumn column = new GenTableColumn();
                column.setColumnName((String) obj[0]);
                column.setColumnComment((String) obj[1]);
                column.setColumnType((String) obj[2]);
                GenUtils.initColumnField(column, table);
                return column;
            }).collect(Collectors.toList()));
            VelocityInitializer.initVelocity();
            VelocityContext context = VelocityUtils.prepareContext(table);

            // 获取模板列表
            List<String> templates = VelocityUtils.getTemplateList();
            for (String template : templates) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            }
        }
        IOUtils.closeQuietly(zip);
        byte[] data = outputStream.toByteArray();
        /**
         * 生成zip文件
         */
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"sys.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }


}
