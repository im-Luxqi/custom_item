package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysInviteLog;

public interface SysInviteLogRepository extends BaseRepository<SysInviteLog, String> {

    long countByInviter(String buyerNick);

}
