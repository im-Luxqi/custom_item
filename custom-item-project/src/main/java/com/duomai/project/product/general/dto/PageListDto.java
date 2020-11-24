package com.duomai.project.product.general.dto;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页插件
 *
 * @description
 * @create by 王星齐
 * @date 2020-09-27 14:37
 */
@Data
public class PageListDto<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int totalAmount;//记录总数
    private int totalPage = 0;//多少页
    private int currentPage = 1;//当前页码
    private int pageSize = 20;//每页显示记录数
    private List<T> resultList;// 当前页数据结果集



    private Integer todayBrowseNum;// 今日已浏览商品数（浏览商品任务用）

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        this.totalPage = (totalAmount % pageSize == 0) ? (totalAmount / pageSize) : (totalAmount / pageSize + 1);
    }

    public void setJpaResultList(Page<T> page) {
        this.resultList = page.get().collect(Collectors.toList());
        this.setTotalAmount((int) page.getTotalElements());
    }

    public void setMybatisResultList(List<T> resultList) {
        PageInfo pageInfo = new PageInfo(resultList);
        this.resultList = pageInfo.getList();
        this.setTotalAmount((int) pageInfo.getTotal());
    }

    public PageListDto startMybatisPage() {
        PageHelper.startPage(currentPage, pageSize);
        return this;
    }

    public Pageable startJPAPage() {
        return PageRequest.of(currentPage - 1, pageSize);
    }
}
