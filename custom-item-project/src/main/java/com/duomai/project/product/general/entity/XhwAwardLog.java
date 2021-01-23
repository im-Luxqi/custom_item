package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖记录
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_award_log")
@org.hibernate.annotations.Table(appliesTo = "xhw_award_log", comment = "抽奖记录")
public class XhwAwardLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '抽奖时间'")
    private Date drawTime;
    @Column(columnDefinition = "DATETIME COMMENT '抽奖时间String'")
    private String drawTimeString;
    @Column(columnDefinition = "varchar(64) COMMENT '玩家混淆昵称'")
    private String buyerNick;
    @Column(columnDefinition = "varchar(64) COMMENT 'ip'")
    private String ip;
    @Column(columnDefinition = "int(1) COMMENT '是否中奖'")
    private Integer win;
}