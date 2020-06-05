package com.duomai.new_custom_base.common.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 */
@Setter
@Getter
public class TableData<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 表格数据对象
     */
    public TableData() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableData(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

}
