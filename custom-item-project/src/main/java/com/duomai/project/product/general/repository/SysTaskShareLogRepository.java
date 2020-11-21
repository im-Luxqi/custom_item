package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskShareLog;

import java.util.Date;

public interface SysTaskShareLogRepository extends BaseRepository<SysTaskShareLog, String> {

    long countByMixSharederAndCreateTimeBetween(String mixShareder, Date start, Date end);
}
