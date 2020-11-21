package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 兑换日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_lucky_exchange_log")
@org.hibernate.annotations.Table(appliesTo = "sys_lucky_exchange_log", comment = "抽奖日志表")
public class SysLuckyExchangeLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(100) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '获取或消耗 0:消耗1:获取'")
    private Integer winOrUse;

//    @Column(nullable = false, columnDefinition = "int(11)  COMMENT '获取或消耗的次数 '")
//    private Integer num;
//    @Column(columnDefinition = "text COMMENT '详情'")
//    private String description;

    @Column(columnDefinition = "varchar(64) COMMENT '奖品Id'")
    private String awardId;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String awardName;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String awardImg;

}