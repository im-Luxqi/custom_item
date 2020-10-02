package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface SysLuckyDrawRecordRepository extends BaseRepository<SysLuckyDrawRecord, String> {

    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndIsWin(String buyerNick, Integer isWin);

    long countByPlayerBuyerNickAndIsWinAndLuckyChanceIsNotNull(String buyerNick, Integer isWin);

    //查询最新50条中奖记录 只返回特定字段
    @Query(nativeQuery = true,
            value = "select award_name,player_buyer_nick,player_znick from sys_lucky_draw_record " +
                    "wehre order by draw_time desc limit 50")
    List<SysLuckyDrawRecord> queryLuckyDrawLog();
}
