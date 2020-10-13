package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysAwardRepository extends BaseRepository<SysAward, String> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update sys_award" +
                    "        set remain_num = remain_num-1,send_num = send_num + 1" +
                    "    where id = ?1" +
                    "        and  remain_num >0")
    int tryReduceOne(String id);

    //获取单个 某个类型的奖品
    SysAward queryFirstByUseWay(AwardUseWayEnum useWay);

    List<SysAward> findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum useWay);

    List<SysAward> findByUseWayAndPoolLevelBeforeOrderByLuckyValueAsc(AwardUseWayEnum useWay, Integer level);
}
