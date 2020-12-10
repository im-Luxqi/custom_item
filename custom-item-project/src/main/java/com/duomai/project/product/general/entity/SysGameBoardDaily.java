package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 互动日常表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_game_board_daily")
@org.hibernate.annotations.Table(appliesTo = "sys_game_board_daily", comment = "互动日常表")
public class SysGameBoardDaily {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(10) COMMENT '创建时间,yyyy-MM-dd格式'")
    private String createTimeString;


    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户混淆昵称'")
    private String buyerNick;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与雪人互动次数'")
    private Integer gameSnowman;

    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和雪人互动 1:是 0：否'")
    private Integer firstGameSnowman;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与企鹅互动次数'")
    private Integer gamePenguin;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和企鹅互动 1:是 0：否'")
    private Integer firstGamePenguin;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与熊互动次数'")
    private Integer gameBear;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和熊互动 1:是 0：否'")
    private Integer firstGameBear;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与彩灯互动次数'")
    private Integer gameLamp;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和灯互动 1:是 0：否'")
    private Integer firstGameLamp;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与狗互动次数'")
    private Integer gameDog;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和狗互动 1:是 0：否'")
    private Integer firstGameDog;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '是否第一次和帐篷互动 1:是 0：否'")
    private Integer firstGameTent;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '与气球互动次数'")
    private Integer gameBalloon;

    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '今日与熊的答题次数'")
    private Integer bearQuestionChance;


    @Column(nullable = false, columnDefinition = "int(10)  COMMENT '每日随机一个数'")
    private Integer todayRandomNum;


}
