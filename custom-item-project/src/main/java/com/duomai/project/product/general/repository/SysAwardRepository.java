package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysAward;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysAwardRepository extends BaseRepository<SysAward, String> {

//    @Query(nativeQuery = true,
//            value = "select * from " +
//                    "(SELECT award_level,award_level_sign,type,name,img,sum(total_num) as total_num" +
//                    "      FROM sys_award" +
//                    "      GROUP BY award_level,award_level_sign,type,name,img) tt " +
//                    " order by tt.award_level_sign")
//    List<Map> findAwardInfo();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update sys_award" +
                    "        set remain_num = remain_num-1,send_num = send_num + 1" +
                    "    where id = ?1" +
                    "        and  remain_num >0")
    int tryReduceOne(String id);


    SysAward findFirstByUseWay(AwardUseWayEnum useWay);

    //上方方法调试未调通 获取单个 某个类型的奖品
    SysAward queryFirstByUseWay(AwardUseWayEnum useWay);

    List<SysAward> findByUseWayOrderByLuckyValueAsc(AwardUseWayEnum useWay);

    List<SysAward> findByUseWayAndPoolLevelBeforeOrderByLuckyValueAsc(AwardUseWayEnum useWay, Integer level);
}
