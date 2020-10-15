package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysLuckyDrawRecord;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface SysLuckyDrawRecordRepository extends BaseRepository<SysLuckyDrawRecord, String> {

    //根据粉丝昵称，是否中奖查询结果集
    List<SysLuckyDrawRecord> findByPlayerBuyerNickAndIsWin(String buyerNick, Integer isWin);

    long countByPlayerBuyerNickAndIsWinAndLuckyChanceIsNotNull(String buyerNick, Integer isWin);

    //查询最新50条中奖记录 只返回特定字段
    @Query(nativeQuery = true,
            value = "select award_name as awardName,player_buyer_nick as playerBuyerNick from sys_lucky_draw_record " +
                    "where is_win = 1 order by draw_time desc limit 50")
    List<Map> queryLuckyDrawLog();

    //获取粉丝某个奖品抽奖日志
    SysLuckyDrawRecord findFirstByPlayerBuyerNickAndAwardId(String buyerNick,String awardId);

    List<SysLuckyDrawRecord> findByPlayerBuyerNick(String buyerNick);


}
