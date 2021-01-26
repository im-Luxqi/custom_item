package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.XhwAward;
import com.duomai.project.product.general.enums.AwardRunningEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface XhwAwardRepository extends BaseRepository<XhwAward, String> {

    XhwAward findFirstByAwardRunningTypeOrderByLevelDesc(AwardRunningEnum running);

    XhwAward findFirstByAwardRunningTypeOrderByLevelAsc(AwardRunningEnum running);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "update xhw_award" +
                    "        set remain_num = remain_num-1,send_num = send_num + 1" +
                    "    where id = ?1" +
                    "        and  remain_num >0")
    int tryReduceOne(String id);

}
