package com.duomai.new_custom_base.api.product.gen.controller;

import com.duomai.new_custom_base.api.product.gen.domain.GenTable;
import com.duomai.new_custom_base.api.product.gen.service.IGenTableService;
import com.duomai.new_custom_base.common.data.TableData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-29 14:27
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController {

    @Autowired
    private IGenTableService genTableService;


    @GetMapping("/list")
    @ResponseBody
    public TableData<GenTable> list(GenTable genTable, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return new TableData<GenTable>(list, new PageInfo(list).getTotal());
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
        String[] tableNames = tables.split(",");
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
    }

    @PostMapping("/remove")
    @ResponseBody
    public void remveTable(String tableId) {
        genTableService.delete().eq(StringUtils.isNotBlank(tableId), GenTable::getTableId, tableId).execute();
    }

    @GetMapping("/db/list")
    @ResponseBody
    public TableData<GenTable> dataList(GenTable genTable, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return new TableData<GenTable>(list, new PageInfo(list).getTotal());
    }

    @GetMapping("/preview/{tableId}")
    @ResponseBody
    public Map<String, String> preview(@PathVariable("tableId") Long tableId) {
        return genTableService.previewCode(tableId);
    }

    @PostMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableId = tables.split(",");
        byte[] data = genTableService.generatorCode(tableId);

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
