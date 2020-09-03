package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysCustom;

public interface SysCustomRepository extends BaseRepository<SysCustom, String> {
    SysCustom findByBuyerNick(String buyerNick);
    SysCustom findByBuyerNickAndOldFans(String buyerNick,Integer oldFans);
}
