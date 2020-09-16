package com.duomai.project.api.gen.controller;

import com.duomai.project.api.gen.entity.CgGenTable;
import com.duomai.project.api.gen.entity.GenTableColumn;
import com.duomai.project.api.gen.repository.GenTableRepository;
import com.duomai.project.api.gen.tools.GenUtils;
import com.duomai.project.api.gen.tools.VelocityInitializer;
import com.duomai.project.api.gen.tools.VelocityUtils;
import com.duomai.project.configuration.SysCustomProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
 * gen 生成
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController {

    @Autowired
    private GenTableRepository genTableRepository;

    @Autowired
    private SysCustomProperties sysCustomProperties;


    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> list(CgGenTable cgGenTable, Integer pageNum, Integer pageSize) {
        Page<CgGenTable> all = genTableRepository.findAll(PageRequest.of(pageNum - 1, pageSize));
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

    /**
     * 可生成表列表
     */
    @GetMapping("/table")
    public String table() {
        Assert.isTrue(sysCustomProperties.isGenFlag(), "暂无权限");
        return "gen/table";
    }

    @PostMapping("/importTable")
    @ResponseBody
    public void importTableSave(String tables) {
        // 查询表信息
        List<Object[]> objects = genTableRepository.selectDbTableListByNames(tables.split(","));
        if (objects != null)
            objects.forEach(obj -> {
                CgGenTable cgGenTable = new CgGenTable();
                cgGenTable.setTableName((String) obj[0]);
                cgGenTable.setTableComment((String) obj[1]);
                GenUtils.initTable(cgGenTable);
                genTableRepository.save(cgGenTable);
            });
    }

    @PostMapping("/remove")
    @ResponseBody
    public String remveTable(String tableId) {
        if (StringUtils.isNotBlank(tableId))
            genTableRepository.deleteById(Long.valueOf(tableId));
        return "success";
    }

    @GetMapping("/db/list")
    @ResponseBody
    public Map<String, Object> dataList(String tableName, String tableComment, Integer pageNum, Integer pageSize) {

        Page<Object[]> all = genTableRepository.selectDbTableList(tableName, tableComment, PageRequest.of(pageNum - 1, pageSize));
        Map<String, Object> result = new HashMap<>();
        result.put("total", all.getTotalElements());
        result.put("rows", all.get().map((obj) -> {
            CgGenTable cgGenTable = new CgGenTable();
            cgGenTable.setTableName((String) obj[0]);
            cgGenTable.setTableComment((String) obj[1]);
            return cgGenTable;
        }).collect(Collectors.toList()));
        return result;
    }

    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public Map<String, String> preview(@PathVariable("tableId") Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        CgGenTable table = genTableRepository.findById(tableId).get();
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
            CgGenTable table = genTableRepository.findById(Long.valueOf(tableId)).get();
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
