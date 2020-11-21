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

    @Column(columnDefinition = "DATETIME COMMENT '排行属性更新的时间'")
    private Date rankingUpdateTime;
    @Column(nullable = false, columnDefinition = "int(11)  COMMENT '排行榜排序基准，比如 积分；成功邀请的人数'")
    private Integer ranking;
    @Column(nullable = false, columnDefinition = "int(11)")
    private Integer rankingReverse;
}
