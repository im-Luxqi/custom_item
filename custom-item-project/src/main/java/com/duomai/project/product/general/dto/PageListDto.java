package com.duomai.project.product.general.dto;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * 采用mybatis 分页组件
 *
 * @description
 * @create by 王星齐
 * @date 2020-09-27 14:37
 */
@Data
public class PageListDto {
    private int totalAmount;//记录总数
    private int totalPage = 0;//多少页
    private int currentPage = 1;//当前页码
    private int pageSize = 20;//每页显示记录数
    private List resultList;// 当前页数据结果集

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        this.totalPage = (totalAmount % pageSize == 0) ? (totalAmount / pageSize) : (totalAmount / pageSize + 1);
    }

    public void setResultList(List resultList) {
        PageInfo pageInfo = new PageInfo(resultList);
        this.resultList = pageInfo.getList();
        this.setTotalAmount((int) pageInfo.getTotal());
    }

    public PageListDto startPage() {
        PageHelper.startPage(currentPage, pageSize);
        return this;
    }
}
