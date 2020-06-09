package com.duomai.new_custom_base.api.product.pagepv.repository;

import com.duomai.new_custom_base.api.product.pagepv.entity.SysPagePvLog;
import com.duomai.new_custom_base.framework.jpa.BaseRepository;

import java.util.List;

public interface SysPagePvLogRepository extends BaseRepository<SysPagePvLog, String> {
    List<SysPagePvLog> findByBuyerNick(String buyerNick);
}
