package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysPagePvLog;

import java.util.List;

public interface SysPagePvLogRepository extends BaseRepository<SysPagePvLog, String> {

    List<SysPagePvLog> findByBuyerNick(String buyerNick);

}
