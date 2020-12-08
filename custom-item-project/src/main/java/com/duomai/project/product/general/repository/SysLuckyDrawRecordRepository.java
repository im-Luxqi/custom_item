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
     * 是否抽过相关奖品
     *
     * @param buyerNick
     * @param luckyChance
     * @return
     */
    long countByPlayerBuyerNickAndLuckyChance(String buyerNick, String luckyChance);


    /**
     * 玩家手上所有的未使用的奶瓶
     *
     * @param buyerNick
     * @param awardTypeEnum
     * @param isWin
     * @param haveExchange
     * @return
     */
    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndAwardTypeAndIsWinAndHaveExchange(String buyerNick, AwardTypeEnum awardTypeEnum, Integer isWin, Integer haveExchange);

    /**
     * 兑换弹幕
     *
     * @return
     */
    @Query(nativeQuery = true,
            value = "select award_name as awardName,player_buyer_nick as playerBuyerNick from sys_lucky_draw_record " +
                    "where award_type in ('COUPON','GOODS')  and  is_win = 1 order by draw_time desc limit 20")
    List<Map> queryExchangeLog();

    /**
     * 我的奖品
     *
     * @param buyernick
     * @return
     */
    @Query(nativeQuery = true,
            value = "select * " +
                    "from  sys_lucky_draw_record " +
                    "where player_buyer_nick = ?1 and award_type in ('COUPON','GOODS')  and is_win = 1   order by draw_time desc ")
    List<SysLuckyDrawRecord> queryMybag(String buyernick);


    /**
     * 消耗 被兑换的商品
     *
     * @param ids
     * @param exchangeTime
     * @return
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update sys_lucky_draw_record" +
                    "        set have_exchange = 1,exchange_time = ?2" +
                    "    where id in (?1) "
    )
    int exchangeAward(String[] ids, Date exchangeTime);


    /**
     * 抽6号瓶子需要先抽5号瓶子
     *
     * @param buyerNick
     * @param isWin
     * @param haveExchange
     * @param awardId
     * @return
     */
    long countByPlayerBuyerNickAndAwardIdAndIsWinAndHaveExchange(String buyerNick, String awardId, Integer isWin, Integer haveExchange);


    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByPlayerBuyerNick(String buyerNick);

    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndIsWin(String buyerNick, Integer booleanYes);
}
