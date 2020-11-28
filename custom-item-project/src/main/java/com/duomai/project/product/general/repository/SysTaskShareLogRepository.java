package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskShareLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface SysTaskShareLogRepository extends BaseRepository<SysTaskShareLog, String> {



    /**
     * 查询 被分享人 某日 助力次数
     *
     * @param mixShareder
     * @param shareTime
     * @return
     */
    long countByMixSharederAndHaveSuccessAndShareTime(String mixShareder, Integer hasSuccess, String shareTime);


    /**
     * 分页查询 分享人分享成功记录
     *
     * @param sharer
     * @param hasSuccess
     * @param of
     * @return
     */
    Page<SysTaskShareLog> findByMixSharerAndHaveSuccessOrderByCreateTimeDesc(String sharer, Integer hasSuccess, Pageable of);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByMixSharer(String buyerNick);

    @Transactional
    void deleteByMixShareder(String buyerNick);
}
