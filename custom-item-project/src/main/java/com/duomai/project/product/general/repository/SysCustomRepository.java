package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustom;

public interface SysCustomRepository extends BaseRepository<SysCustom, String> {

    //根据粉丝昵称获取粉丝信息
    SysCustom findByBuyerNick(String buyerNick);

    //用于完善是否关注字段查询
    SysCustom findByBuyerNickAndHistoryFollow(String buyerNick,Integer oldFans);

}
