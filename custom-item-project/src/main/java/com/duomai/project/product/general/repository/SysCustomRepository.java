package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import org.springframework.transaction.annotation.Transactional;

public interface SysCustomRepository extends BaseRepository<SysCustom, String> {

    /**
     * 查询  粉丝信息
     *
     * @param buyerNick
     * @return
     */
    SysCustom findByBuyerNick(String buyerNick);

    /**
     * 查询  指定未知关注状态的玩家
     *
     * @param buyerNick
     * @param followWayFrom
     * @return
     */
    SysCustom findFirstByBuyerNickAndFollowWayFrom(String buyerNick, FollowWayFromEnum followWayFrom);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
