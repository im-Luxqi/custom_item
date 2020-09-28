package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 邀请日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_invite_log")
@org.hibernate.annotations.Table(appliesTo = "sys_invite_log", comment = "邀请日志表")
public class SysInviteLog {
    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(47) COMMENT '邀请人mix'")
    private String mixInviter;

    @Column(columnDefinition = "varchar(47) COMMENT '邀请人'")
    private String inviter;

    @Column(columnDefinition = "varchar(47) COMMENT '被邀请人mix'")
    private String mixInvitee;

    @Column(columnDefinition = "varchar(47) COMMENT '被邀请人'")
    private String invitee;

    @Column(columnDefinition = "varchar(47) COMMENT '被邀请人头像'")
    private String inviteeImg;

}
