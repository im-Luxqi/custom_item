package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwAwardRecord;

import java.util.List;

public interface XhwAwardRecordRepository extends BaseRepository<XhwAwardRecord, String> {

    List<XhwAwardRecord> findByBuyerNickOrderByDrawTimeDesc();
}
