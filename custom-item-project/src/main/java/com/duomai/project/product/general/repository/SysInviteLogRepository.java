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
    List<SysInviteLog> findByInviterAndInvitationTypeOrderByCreateTimeDesc(String buyerNick, InvitationTypeEnum invitationType);

    //获取被邀请者信息
    SysInviteLog queryFirstByInviteeAndInvitationType(String buyerNick, InvitationTypeEnum invitationType);


    //清除数据
    List<SysInviteLog> findByInviter(String buyerNick);
    List<SysInviteLog> findByInvitee(String invitee);

}
