package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;

import java.util.List;

public interface SysLuckyDrawRecordRepository extends BaseRepository<SysLuckyDrawRecord, String> {

    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndIsWin(String buyerNick,Integer isWin);
}
