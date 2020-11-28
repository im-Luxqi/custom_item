package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyExchangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface SysExchangeLogRepository extends BaseRepository<SysLuckyExchangeLog, String> {


    /**
     * 分页查询 指定玩家 瓶子获取消耗明细
     *
     * @param buyerNick
     * @param startJPAPage
     * @return
     */
    Page<SysLuckyExchangeLog> findByBuyerNickOrderByCreateTimeAsc(String buyerNick, Pageable startJPAPage);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
