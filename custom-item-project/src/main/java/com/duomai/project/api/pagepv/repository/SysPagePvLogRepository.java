package com.duomai.project.api.pagepv.repository;

import com.duomai.project.api.pagepv.entity.SysPagePvLog;
import com.duomai.project.framework.jpa.BaseRepository;

import java.util.List;

public interface SysPagePvLogRepository extends BaseRepository<SysPagePvLog, String> {
    List<SysPagePvLog> findByBuyerNick(String buyerNick);
}
