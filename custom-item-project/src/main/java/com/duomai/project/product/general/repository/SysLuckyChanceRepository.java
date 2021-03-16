package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    /**
     * 查询  玩家 剩余抽奖次数
     *
     * @param buyerNick
     * @param use
     * @return
     */
    long countByBuyerNickAndIsUse(String buyerNick, Integer use);

    /**
     * 查询  玩家 一条未使用的抽奖机会日志
     *
     * @param buyerNick
     * @param use
     * @return
     */
    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    //获取某个任务当天完成了几次
//    long countByBuyerNickAndChanceFromAndGetTimeBetween(String buyerNick, LuckyChanceFromEnum chanceFrom, Date startTime, Date endTime);


    /**
     * 查询 玩家 指定来源 某日的游戏机会获取数量
     *
     * @param buyerNick
     * @param chanceFrom
     * @param sendTime
     * @return
     */
    long countByBuyerNickAndChanceFromAndGetTimeString(String buyerNick, LuckyChanceFromEnum chanceFrom, String sendTime);


    /**
     * 查询 玩家 指定来源 的游戏机会获取数量
     *
     * @param buyerNick
     * @param from
     * @return
     */
    long countByBuyerNickAndChanceFrom(String buyerNick, LuckyChanceFromEnum from);


    /**
     * 查询 玩家 未展示过的常见机会
     *
     * @param buyerNick
     * @param haveNotification
     * @return
     */
    List<SysLuckyChance> findByBuyerNickAndHaveNotification(String buyerNick, Integer haveNotification);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);

    long countByBuyerNickAndIsUseAndUseTimeBetween(String buyerNick, Integer isUse, Date start, Date end);

    /**
     * 查询所有未使用的卡牌
     *
     * @param buyerNick
     * @param booleanNo
     * @description
     * @create by 王星齐
     * @time 2021-03-16 14:22:23
     */
    List<SysLuckyChance> findByBuyerNickAndIsUse(String buyerNick, Integer booleanNo);
}
