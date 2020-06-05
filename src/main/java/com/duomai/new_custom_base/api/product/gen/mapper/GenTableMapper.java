package com.duomai.new_custom_base.api.product.gen.mapper;

import com.duomai.new_custom_base.api.product.gen.domain.GenTable;
import com.duomai.new_custom_base.api.product.gen.domain.GenTableColumn;
import com.duomai.new_custom_base.framework.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务 数据层
 *
 * @author Crown
 */
@Mapper
public interface GenTableMapper extends BaseMapper<GenTable> {
    List<GenTable> selectDbTableList(GenTable genTable);

    List<GenTable> selectDbTableListByNames(String[] tableNames);

    List<GenTableColumn> selectDbTableColumnsByName(String tableName);
}