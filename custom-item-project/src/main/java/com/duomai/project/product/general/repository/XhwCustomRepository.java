package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwCustom;

public interface XhwCustomRepository extends BaseRepository<XhwCustom, String> {

    XhwCustom findByBuyerNick(String buyerNick);
}
