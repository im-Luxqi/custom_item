package com.duomai.new_custom_base.api.product.gen.service;

import com.duomai.new_custom_base.api.product.gen.domain.GenTable;
import com.duomai.new_custom_base.framework.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 业务 服务层
 */
public interface IGenTableService extends BaseService<GenTable> {
    List<GenTable> selectGenTableList(GenTable genTable);

    List<GenTable> selectDbTableList(GenTable genTable);

    List<GenTable> selectDbTableListByNames(String[] tableNames);

    void importGenTable(List<GenTable> tableList);

    Map<String, String> previewCode(Long tableId);

    byte[] generatorCode(String[] tableId);
}
