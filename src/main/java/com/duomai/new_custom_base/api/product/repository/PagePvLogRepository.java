package com.duomai.new_custom_base.api.product.repository;

import com.duomai.new_custom_base.framework.jpa.BaseRepository;
import com.duomai.new_custom_base.api.product.entity.PagePvLog;

import java.util.List;

public interface PagePvLogRepository extends BaseRepository<PagePvLog, String> {
    List<PagePvLog> findByBuyerNick(String buyerNick);
}
