package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyChance;
import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysLuckyChanceRepository extends BaseRepository<SysLuckyChance, String> {


    /**
     * 查询 玩家 指定来源 的游戏机会获取数量
     *
     * @param buyerNick
     * @param from
     * @return
     */
    long countByHaveSuccessAndBuyerNickAndChanceFrom(Integer haveSuccess, String buyerNick, LuckyChanceFromEnum from);


    /**
     * 查询 玩家 指定来源 某日的游戏机会获取数量
     *
     * @param buyerNick
     * @param chanceFrom
     * @param sendTime
     * @return
     */
    long countByHaveSuccessAndBuyerNickAndChanceFromAndGetTimeString(Integer haveSuccess, String buyerNick, LuckyChanceFromEnum chanceFrom, String sendTime);


    /**
     * 查询 玩家 未展示过的常见机会
     *
     * @param buyerNick
     * @param haveNotification
     * @return
     */
    List<SysLuckyChance> findByBuyerNickAndHaveNotification(String buyerNick, Integer haveNotification);


    /**
     * 查询所有未使用的卡牌
     *
     * @param buyerNick
     * @param booleanNo
     * @description
     * @create by 王星齐
     * @time 2021-03-16 14:22:23
     */
    List<SysLuckyChance> findByHaveSuccessAndBuyerNickAndIsUse(Integer haveSuccess, String buyerNick, Integer booleanNo);

    /**
     * 查询卡片使用情况根据使用时间倒序
     *
     * @param buyerNick
     * @param isUser
     * @return
     */
    Page<SysLuckyChance> findByHaveSuccessAndBuyerNickAndIsUseOrderByUseTimeDesc(Integer haveSuccess, String buyerNick, Integer isUser, Pageable of);


    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
