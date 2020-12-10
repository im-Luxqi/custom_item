package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 排行表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_custom_ranking")
@org.hibernate.annotations.Table(appliesTo = "sys_custom_ranking", comment = "粉丝排行榜表")
public class SysCustomRanking implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(columnDefinition = "varchar(64) COMMENT '真实昵称'")
    private String znick;
    @Column(columnDefinition = "varchar(255) COMMENT '头像'")
    private String headImg;



    @Column(columnDefinition = "int(20) COMMENT '排名'")
    private Integer winAwardRank;

    @Column(columnDefinition = "varchar(255) COMMENT '中奖的Id'")
    private String winAwardId;

    @Column(columnDefinition = "varchar(255) COMMENT '中奖的奖品名称'")
    private String winAwardName;

    @Column(columnDefinition = "int(1) COMMENT '是否发送成功'")
    private Integer sendSuccess;

}
