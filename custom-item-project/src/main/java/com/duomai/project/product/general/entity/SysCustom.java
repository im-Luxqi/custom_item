package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 粉丝参与表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_custom")
@org.hibernate.annotations.Table(appliesTo = "sys_custom", comment = "粉丝参与表")
public class SysCustom {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;
    @Column(nullable = false, columnDefinition = "varchar(255) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(columnDefinition = "varchar(64) COMMENT '用户openID'")
    private String openId;
    @Column(columnDefinition = "varchar(64) COMMENT '真实昵称'")
    private String znick;
    @Column(columnDefinition = "varchar(255) COMMENT '头像'")
    private String headImg;

//    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否为会员 0:否1:是'")
//    private Integer member;
//    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否老会员 0:否1:是'")
//    private Integer historyMember;
//    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否粉丝(关注店铺) 0:否1:是'")
//    private Integer follow;
//    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否老粉丝 0:否1:是'")
//    private Integer historyFollow;
    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否授权 0:否1:是'")
    private Integer haveAuthorization;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '入会途径'")
    private MemberWayFromEnum memberWayFrom;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '入会途径'")
    private FollowWayFromEnum followWayFrom;

}
