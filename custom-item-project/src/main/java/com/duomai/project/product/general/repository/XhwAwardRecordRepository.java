package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.entity.XhwAwardRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface XhwAwardRecordRepository extends BaseRepository<XhwAwardRecord, String> {

    long countByBuyerNickAndAwardId(String buyerNick, String id);

    List<XhwAwardRecord> findByBuyerNickOrderByDrawTimeDesc(String buyerNick);
    //查询最新50条中奖记录 只返回特定字段
    @Query(nativeQuery = true,
            value = "select award_name as awardName, recevice_time as receviceTime,recevice_phone as recevicePhone from xhw_award_record " +
                    "where is_Fill = 1 order by recevice_time desc limit 20")
    List<Map> queryLuckyDrawLog();

}
