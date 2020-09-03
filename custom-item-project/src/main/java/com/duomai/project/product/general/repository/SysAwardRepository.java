package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysAward;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface SysAwardRepository extends BaseRepository<SysAward, String> {

    @Query(nativeQuery = true,
            value = "select * from " +
                    "(SELECT award_level,award_level_sign,type,name,img,sum(total_num) as total_num" +
                    "      FROM sys_award" +
                    "      GROUP BY award_level,award_level_sign,type,name,img) tt " +
                    " order by tt.award_level_sign")
    List<Map> findAwardInfo();
}
