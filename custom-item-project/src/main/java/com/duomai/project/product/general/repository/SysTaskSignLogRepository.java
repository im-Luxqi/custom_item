package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskSignLog;

/**
 * @author 王星齐
 */
public interface SysTaskSignLogRepository extends BaseRepository<SysTaskSignLog, String> {

    /**
     * 查询 玩家 累计签到次数
     *
     * @param buyerNick 玩家
     * @return
     */
    long countByBuyerNick(String buyerNick);

    /**
     * 查询 玩家 指定哪天的 签到记录
     *
     * @param buyerNick 玩家
     * @param signTime  指定哪天  格式yyyy-MM-dd
     * @return
     */
    SysTaskSignLog findFirstByBuyerNickAndSignTime(String buyerNick, String signTime);
}
