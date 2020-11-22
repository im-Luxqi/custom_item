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

    //根据粉丝昵称，是否中奖查询结果集
    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndIsWin(String buyerNick, Integer isWin);

    long countByPlayerBuyerNickAndIsWinAndLuckyChanceIsNotNull(String buyerNick, Integer isWin);

    //查询最新50条中奖记录 只返回特定字段
    @Query(nativeQuery = true,
            value = "select award_name as awardName,player_buyer_nick as playerBuyerNick from sys_lucky_draw_record " +
                    "where is_win = 1 order by draw_time desc limit 50")
    List<Map> queryLuckyDrawLog();

    //获取粉丝某个奖品抽奖日志
    SysLuckyDrawRecord findFirstByPlayerBuyerNickAndAwardId(String buyerNick, String awardId);

    List<SysLuckyDrawRecord> findByPlayerBuyerNick(String buyerNick);


    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchange(String buyerNick, AwardTypeEnum awardTypeEnum, Integer isWin, Integer haveExchange);

    @Query(nativeQuery = true,
            value = "select award_name as awardName,player_buyer_nick as playerBuyerNick from sys_lucky_draw_record " +
                    "where is_win = 1 and award_type in ('COUPON','GOODS')  order by exchange_time desc limit 20")
    List<Map> queryExchangeLog();

    @Query(nativeQuery = true,
            value = "select * " +
                    "from  sys_lucky_draw_record " +
                    "where player_buyer_nick = ?1  and is_win = 1 and award_type in ('COUPON','GOODS')  order by award_id asc ")
    List<SysLuckyDrawRecord> queryMybag(String buyernick);


    long countByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchangeAndAwardId(String buyerNick, AwardTypeEnum awardTypeEnum, Integer isWin, Integer haveExchange, String awardId);

    long countByPlayerBuyerNickAndAwardTypeAndIsWinAndDrawTimeBetween(String buyerNick, AwardTypeEnum awardTypeEnum, Integer isWin, Date start, Date end);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update sys_lucky_draw_record" +
                    "        set have_exchange = 1,exchange_time = ?2" +
                    "    where id in (?1) "
    )
    int exchangeAward(String[] ids, Date exchangeTime);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByPlayerBuyerNick(String buyerNick);
}
