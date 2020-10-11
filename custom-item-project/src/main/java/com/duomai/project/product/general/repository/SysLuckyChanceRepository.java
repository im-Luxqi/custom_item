package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;

import java.util.Date;
import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {

    long countByBuyerNickAndIsUse(String buyerNick, Integer use);


    SysLuckyChance findFirstByBuyerNickAndIsUse(String buyerNick, Integer use);

    List<SysLuckyChance> findAllByBuyerNickAndChanceFrom(String buyerNick, LuckyChanceFromEnum from);

    List<SysLuckyChance> findAllByBuyerNick(String buyerNick);

    long countByBuyerNickAndGetTimeBetween(String buyerNick, Date startTime, Date endTime);

    //获取某个任务当天完成了几次
    long countByBuyerNickAndChanceFromAndGetTimeBetween(String buyerNick, LuckyChanceFromEnum chanceFrom, Date startTime, Date endTime);

}
