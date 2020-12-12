package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysGameBoardDaily;
import org.springframework.transaction.annotation.Transactional;

public interface SysGameBoardDailyRepository extends BaseRepository<SysGameBoardDaily, String> {
//    /**
//     *  count 当日 是否创建 board
//     * @param buyerNick
//     * @param requestStartTimeString
//     * @return
//     */
//    long countByBuyerNickAndCreateTimeString(String buyerNick, String requestStartTimeString);

    /**
     *  查询 当日 board
     * @param buyerNick
     * @param requestStartTimeString
     * @return
     */
    SysGameBoardDaily findFirstByBuyerNickAndCreateTimeString(String buyerNick, String requestStartTimeString);

    @Transactional
    void deleteByBuyerNick(String buyerNick);
}
