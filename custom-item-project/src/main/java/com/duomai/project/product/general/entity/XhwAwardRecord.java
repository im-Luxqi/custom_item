package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 中奖记录
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_award_record")
@org.hibernate.annotations.Table(appliesTo = "xhw_award_record", comment = "中奖记录")
public class XhwAwardRecord implements Serializable {
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

    @Column(columnDefinition = "varchar(64) COMMENT '奖品Id'")
    private String awardId;

    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String awardName;

    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String awardImg;

    @Column(nullable = false, columnDefinition = "int(1) COMMENT '是否填写地址'")
    private Integer isFill;

    @Column(columnDefinition = "varchar(20) COMMENT '收件人昵称'")
    private String receviceName;
    @Column(columnDefinition = "varchar(64) COMMENT '收件人电话'")
    private String recevicePhone;
    @Column(columnDefinition = "varchar(64) COMMENT '收件人地址 市'")
    private String receviceCity;
    @Column(columnDefinition = "varchar(64) COMMENT '收件人地址 省'")
    private String receviceProvince;
    @Column(columnDefinition = "varchar(64) COMMENT '收件人地址 区'")
    private String receviceDistrict;

    @Column(columnDefinition = "text COMMENT '收件人地址 详细地址'")
    private String receviceAddress;

    @Column(columnDefinition = "DATETIME COMMENT '填写地址时间'")
    private Date receviceTime;

    @Column(columnDefinition = "DATETIME COMMENT '填写地址时间'")
    private String receviceTimeString;
    @Column(columnDefinition = "varchar(64) COMMENT 'ip'")
    private String ip;
}