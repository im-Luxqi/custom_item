package com.duomai.project.api.pagepv.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.api.pagepv.entity.SysPagePvLog;

import java.util.List;

public interface SysPagePvLogRepository extends BaseRepository<SysPagePvLog, String> {
    List<SysPagePvLog> findByBuyerNick(String buyerNick);
}
