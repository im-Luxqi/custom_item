package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskShareLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface SysTaskShareLogRepository extends BaseRepository<SysTaskShareLog, String> {


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

    /**
     * 分享人  某天  被分享次数
     *
     * @param inviter
     * @param inviteTime
     * @description
     * @create by 王星齐
     * @time 2021-03-16 16:44:03
     */

    long countByMixSharerAndHaveSuccessAndShareTimeAndMixShareder(String inviter, Integer hasSuccess, String shareTime, String invitee);

    /**
     * 查询 分享人  分享次数
     *
     * @return
     */
    long countByMixSharerAndHaveSuccess(String inviter, Integer booleanYes);

    long countByMixSharederAndHaveSuccessAndShareTime(String inviter, Integer booleanYes, String todayString);
}
