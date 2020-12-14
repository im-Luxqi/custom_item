package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustomRanking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SysCustomRankingRepository extends BaseRepository<SysCustomRanking, String> {


    @Query(nativeQuery = true,
            value = "SELECT count(*) " +
                    "FROM `sys_custom_ranking`" +
                    "WHERE id <= ?1 "
    )
    long rankingWhere(Integer id);


    SysCustomRanking findFirstByBuyerNick(String buyerNick);

    long countByBuyerNick(String buyerNick);

    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
