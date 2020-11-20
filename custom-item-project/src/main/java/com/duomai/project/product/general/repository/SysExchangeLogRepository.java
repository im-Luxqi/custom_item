package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.entity.SysExchangeLog;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysExchangeLogRepository extends BaseRepository<SysExchangeLog, String> {

}
