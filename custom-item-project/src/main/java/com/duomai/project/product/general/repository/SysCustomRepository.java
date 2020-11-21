package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustom;
import com.duomai.project.product.general.enums.FollowWayFromEnum;

public interface SysCustomRepository extends BaseRepository<SysCustom, String> {

    //根据粉丝昵称获取粉丝信息
    SysCustom findByBuyerNick(String buyerNick);

    /**
     * 查询  指定未知关注状态的玩家
     *
     * @param buyerNick
     * @param followWayFrom
     * @return
     */
    SysCustom findFirstByBuyerNickAndFollowWayFrom(String buyerNick, FollowWayFromEnum followWayFrom);
}
