package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysInviteLog;

import java.util.List;

public interface SysInviteLogRepository extends BaseRepository<SysInviteLog, String> {

    //根据粉丝昵称count
    long countByInviter(String buyerNick);

    //获取该粉丝邀请记录
    List<SysInviteLog> findByInviter(String buyerNick);

}
