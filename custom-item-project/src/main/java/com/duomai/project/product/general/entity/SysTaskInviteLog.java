package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.InviteTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 邀请日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_task_invite_log")
@org.hibernate.annotations.Table(appliesTo = "sys_task_invite_log", comment = "邀请日志表")
public class SysTaskInviteLog  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(10) COMMENT '邀请时间，yyyy-MM-dd格式'")
    private String inviteTime;

    @Column(columnDefinition = "varchar(100) COMMENT '邀请人mix'")
    private String mixInviter;

    @Column(columnDefinition = "varchar(100) COMMENT '邀请人'")
    private String inviter;

    @Column(columnDefinition = "varchar(255) COMMENT '被邀请人头像'")
    private String inviterImg;

    @Column(columnDefinition = "varchar(100) COMMENT '被邀请人mix'")
    private String mixInvitee;

    @Column(columnDefinition = "varchar(100) COMMENT '被邀请人'")
    private String invitee;

    @Column(columnDefinition = "varchar(255) COMMENT '被邀请人头像'")
    private String inviteeImg;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '邀请类型'")
    private InviteTypeEnum inviteType;
    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否邀请成功 0:否1:是'")
    private Integer haveSuccess;
}
