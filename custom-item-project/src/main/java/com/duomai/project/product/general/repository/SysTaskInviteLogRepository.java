package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysTaskInviteLogRepository extends BaseRepository<SysTaskInviteLog, String> {

    //清除数据
    List<SysTaskInviteLog> findByInviter(String buyerNick);

    List<SysTaskInviteLog> findByInvitee(String invitee);


    /**
     * 分页查询 邀请人邀请成功记录
     *
     * @param buyerNick
     * @return
     */
    Page<SysTaskInviteLog> findByMixInviterAndHaveSuccessOrderByCreateTimeDesc(String buyerNick, Integer haveSuccess, Pageable of);

    /**
     * test
     *
     * @param buyerNick
     */
    @Transactional
    void deleteByMixInviter(String buyerNick);
    @Transactional
    void deleteByMixInvitee(String buyerNick);
}
