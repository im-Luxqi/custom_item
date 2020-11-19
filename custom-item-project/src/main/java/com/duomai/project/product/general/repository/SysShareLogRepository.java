package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysShareLog;

import java.util.Date;
import java.util.List;

public interface SysShareLogRepository extends BaseRepository<SysShareLog, String> {

    long countByMixSharederAndCreateTimeBetween(String mixShareder, Date start, Date end);
}
