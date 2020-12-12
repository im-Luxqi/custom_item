package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustomRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysCustomRankingRepository extends BaseRepository<SysCustomRanking, String> {



    @Query(nativeQuery = true,
            value = "SELECT count(*) " +
                    "FROM `sys_custom_ranking`" +
                    "WHERE  id <= ?1 "
    )
    long rankingWhere(Integer id);


    SysCustomRanking findFirstByBuyerNick(String buyerNick);


    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
