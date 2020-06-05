package com.duomai.new_custom_base.api.product.pagepv.repository;

import com.duomai.new_custom_base.api.product.pagepv.entity.PagePvLog;
import com.duomai.new_custom_base.framework.jpa.BaseRepository;

import java.util.List;

public interface PagePvLogRepository extends BaseRepository<PagePvLog, String> {
    List<PagePvLog> findByBuyerNick(String buyerNick);
}
