package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;

import java.util.Date;
import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    //获取当前粉丝剩余抽奖次数
    long countByBuyerNickAndIsUse(String buyerNick, Integer use);

    //获取一条未使用的抽奖机会日志
    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    //获取某个任务当天完成了几次
    long countByBuyerNickAndChanceFromAndGetTimeBetween(String buyerNick, LuckyChanceFromEnum chanceFrom, Date startTime, Date endTime);

    List<SysLuckyChance> findByBuyerNick(String buyerNick);

    void deleteByBuyerNick(String buyerNick);

}
