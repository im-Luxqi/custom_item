package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskShareLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysTaskShareLogRepository extends BaseRepository<SysTaskShareLog, String> {

    long countByMixSharederAndCreateTimeBetween(String mixShareder, Date start, Date end);

    /**
     * 分页查询 分享人分享成功记录
     *
     * @param buyerNick
     * @param sharer
     * @param of
     * @return
     */
    Page<SysTaskShareLog> findByMixSharerAndHaveSuccessOrderByCreateTimeDesc(String buyerNick, Integer sharer, Pageable of);

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
