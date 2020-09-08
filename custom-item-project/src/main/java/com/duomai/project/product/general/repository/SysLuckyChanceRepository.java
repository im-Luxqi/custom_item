package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    long countByBuyerNickAndIsUse(String buyerNick, Integer use);

    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    List<SysLuckyChance> findAllByBuyerNick(String buyerNick);

}
