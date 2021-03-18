package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysTaskInviteLog;
import com.duomai.project.product.general.enums.InviteTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysTaskInviteLogRepository extends BaseRepository<SysTaskInviteLog, String> {


    /**
     * 分页查询 邀请人邀请成功记录
     *
     * @param buyerNick
     * @return
     */
    Page<SysTaskInviteLog> findByInviteTypeAndMixInviterAndHaveSuccessOrderByCreateTimeDesc(InviteTypeEnum inviteType, String buyerNick, Integer haveSuccess, Pageable of);

    /**
     * @description 
     * @create by 王星齐
     * @time 2021-03-16 18:40:10
     * @param inviteType
     * @param inviter
     * @param haveSuccess
     * @param invitee
     */
    long countByInviteTypeAndMixInviteeAndHaveSuccessAndMixInviter(InviteTypeEnum inviteType, String invitee, Integer haveSuccess, String inviter);
    long countByInviteTypeAndMixInviteeAndHaveSuccess(InviteTypeEnum inviteType, String invitee, Integer haveSuccess);
    long countByInviteTypeAndMixInviterAndHaveSuccess(InviteTypeEnum inviteFollow, String inviter, Integer booleanYes);

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
