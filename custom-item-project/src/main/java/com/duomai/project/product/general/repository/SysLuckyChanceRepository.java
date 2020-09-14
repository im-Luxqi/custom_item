package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;

import java.util.Date;
import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    long countByBuyerNickAndIsUse(String buyerNick, Integer use);

    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    List<SysLuckyChance> findAllByBuyerNick(String buyerNick);

    long countByBuyerNickAndGetTimeBetween(String buyerNick, Date startTime, Date endTime);

}
