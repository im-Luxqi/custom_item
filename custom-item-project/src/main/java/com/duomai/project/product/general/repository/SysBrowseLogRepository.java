package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysBrowseLog;
import com.duomai.project.product.general.enums.TaskTypeEnum;

import java.util.Date;
import java.util.List;

public interface SysBrowseLogRepository extends BaseRepository<SysBrowseLog, String> {

    List<SysBrowseLog> findByBuyerNickAndCreateTimeBetweenAndNumId(String buyerNick, Date start, Date end, Long numId);

    List<SysBrowseLog> findByBuyerNickAndCreateTimeBetween(String buyerNick, Date start, Date end);

}
