package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_lucky_draw_record")
@org.hibernate.annotations.Table(appliesTo = "sys_lucky_draw_record", comment = "抽奖日志表")
public class SysLuckyDrawRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品机会'")
    private String luckyChance;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '抽奖时间'")
    private Date drawTime;
    @Column(columnDefinition = "DATETIME COMMENT '抽奖时间String'")
    private String drawTimeString;
    @Column(columnDefinition = "varchar(255) COMMENT '玩家头像'")
    private String playerHeadImg;
    @Column(columnDefinition = "varchar(64) COMMENT '玩家混淆昵称'")
    private String playerBuyerNick;
    @Column(columnDefinition = "varchar(64) COMMENT '玩家真实昵称'")
    private String playerZnick;

    @Column(columnDefinition = "varchar(64) COMMENT '奖品Id'")
    private String awardId;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品等级'")
    private String awardLevel;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String awardName;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String awardImg;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '奖品类型'")
    private AwardTypeEnum awardType;
    @Column(nullable = false, columnDefinition = "int(1) COMMENT '是否中奖'")
    private Integer isWin;
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

    @Column(columnDefinition = "varchar(64) COMMENT '身份证'")
    private String identityNumber;

    @Column(columnDefinition = "text COMMENT '收件人地址 详细地址'")
    private String receviceAddress;


    @Column(columnDefinition = "DATETIME COMMENT '填写地址时间'")
    private Date receviceTime;
    @Column(columnDefinition = "text COMMENT '发奖发送错误信息'")
    private String sendError;
    @Column(columnDefinition = "text COMMENT '备注'")
    private String remark;

    @Column(columnDefinition = "int(1)  COMMENT '虚拟奖品是否兑换 0:否1:是'")
    private Integer haveExchange;

    @Column(columnDefinition = "DATETIME COMMENT '兑换时间'")
    private Date exchangeTime;



    public SysLuckyDrawRecord() {
    }

    public SysLuckyDrawRecord(String s) {
        this.awardId = s;
    }
}