package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.LuckyChanceFromEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 抽奖机会表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_lucky_chance")
@org.hibernate.annotations.Table(appliesTo = "sys_lucky_chance", comment = "抽奖机会表")
public class SysLuckyChance {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '获取时间'")
    private Date getTime;
    @Column(columnDefinition = "DATETIME COMMENT '消耗时间'")
    private Date useTime;
    @Column(nullable = false, columnDefinition = "varchar(47) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '机会来源'")
    private LuckyChanceFromEnum chanceFrom;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '是否使用'")
    private Integer isUse;

    @Column(columnDefinition = "varchar(64) COMMENT '订单号'")
    private String tid;
    @Column(columnDefinition = "DATETIME COMMENT '下单时间'")
    private Date tidTime;

}
