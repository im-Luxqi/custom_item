package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysBrowseLog;

import java.util.Date;
import java.util.List;

public interface SysBrowseLogRepository extends BaseRepository<SysBrowseLog, String> {

    //查询该商品在时间内是否被浏览过
    SysBrowseLog findFirstByBuyerNickAndCreateTimeBetweenAndNumId(String buyerNick, Date start, Date end, Long numId);

    //查询该粉丝在时间内浏览的日志
    List<SysBrowseLog> findByBuyerNickAndCreateTimeBetween(String buyerNick, Date start, Date end);
}
