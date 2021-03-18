package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import com.duomai.project.product.general.enums.AwardTypeEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SysLuckyDrawRecordRepository extends BaseRepository<SysLuckyDrawRecord, String> {



    /**
     * 抽奖弹幕
     *
     * @return
     */
    @Query(nativeQuery = true,
            value = "select award_name as awardName,player_buyer_nick as playerBuyerNick from sys_lucky_draw_record " +
                    "where is_win = 1 order by draw_time desc limit 20")
    List<Map> queryDrawLog();

    /**
     * 我的奖品
     *
     * @param buyernick
     * @return
     */
    @Query(nativeQuery = true,
            value = "select * " +
                    "from  sys_lucky_draw_record " +
                    "where player_buyer_nick = ?1   and is_win = 1   order by draw_time desc ")
    List<SysLuckyDrawRecord> queryMybag(String buyernick);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByPlayerBuyerNick(String buyerNick);

    long countByPlayerBuyerNickAndAwardId(String buyerNick,String id);
}
