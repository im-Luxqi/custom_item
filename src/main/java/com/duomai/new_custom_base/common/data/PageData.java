package com.duomai.new_custom_base.common.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 * @create by 王星齐
 * @date 2020-05-29 14:35
 */
@Data
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<T> rows;


    public PageData() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageData(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

}
