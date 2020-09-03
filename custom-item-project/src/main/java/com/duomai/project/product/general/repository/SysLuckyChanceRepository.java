package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    long countByBuyerNickAndIsUse(String buyerNick, Integer use);

    long countByTid(String tid);

    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update sys_award" +
                    "        set remain_num = remain_num-1,send_num = send_num + 1" +
                    "    where id = ?1" +
                    "        and  remain_num >0")
    int tryReduceOne(String id);
}
