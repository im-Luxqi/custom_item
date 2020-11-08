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
            value = "SELECT * " +
                    "FROM `sys_custom_ranking` " +
                    "ORDER BY  ranking_reverse,ranking_update_time,id ",
            countQuery = "SELECT count(*) " +
                    "FROM `sys_custom_ranking` "
    )
    Page<SysCustomRanking> ranking(Pageable of);


    @Query(nativeQuery = true,
            value = "SELECT count(*) " +
                    "FROM `sys_custom_ranking`" +
                    "WHERE  ranking_reverse < ?1 " +
                    "       or ranking_reverse = ?1 && ranking_update_time < ?2" +
                    "       or ranking_reverse = ?1 && ranking_update_time = ?2  && id<=?3"
    )
    long whoRankingWhere(Integer whoRankingReverse, Date rankingUpdateTime, Integer whoRankingId);


    SysCustomRanking findFirstByBuyerNick(String buyerNick);


    @Transactional
    @Modifying
    @Query("update SysCustomRanking m set m.znick=?1,m.headImg=?2 where  m.buyerNick=?3")
    void updateZnickAndHeadImgByBuyerNick(String znick, String headImg, String buyerNick);

}
