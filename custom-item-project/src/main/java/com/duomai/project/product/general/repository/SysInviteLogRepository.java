package com.duomai.project.product.general.repository;

import com.duomai.common.framework.jpa.BaseRepository;
import com.duomai.project.product.general.entity.SysInviteLog;
import com.duomai.project.product.general.enums.InvitationTypeEnum;

import java.util.List;

public interface SysInviteLogRepository extends BaseRepository<SysInviteLog, String> {

    //根据粉丝昵称 获取是否被邀请过 count
    long countByInviteeAndInvitationType(String buyerNick,InvitationTypeEnum invitationTypeEnum);

    //获取邀请的个数
    long countByInviterAndInvitationType(String buyerNick,InvitationTypeEnum invitationTypeEnum);

    //获取该粉丝邀请记录
    List<SysInviteLog> findByInviterAndInvitationType(String buyerNick, InvitationTypeEnum invitationType);

}
