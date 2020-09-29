package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 奖品表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_award")
@org.hibernate.annotations.Table(appliesTo = "sys_award", comment = "奖品表")
public class SysAward {
    @Id
    @Column(length = 64)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) COMMENT '奖品用途'")
    private AwardUseWayEnum useWay;
    @Column(columnDefinition = "varchar(47) COMMENT '奖品等级;一等奖、二等奖。。。'")
    private String awardLevel;
    @Column(columnDefinition = "int(2) COMMENT '奖品等级sign'")
    private Integer awardLevelSign;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) COMMENT '奖品类型'")
    private AwardTypeEnum type;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String img;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品价格'")
    private String price;

    @Column(columnDefinition = "int(10) COMMENT '奖品总数量'")
    private Integer totalNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品剩余数量'")
    private Integer remainNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品已发数量'")
    private Integer sendNum;

    @Column(columnDefinition = "varchar(20) COMMENT '奖品概率'")
    private String luckyValue;
    @Column(columnDefinition = "int(2) COMMENT '奖池等级'")
    private Integer poolLevel;
    @Column(columnDefinition = "varchar(64) COMMENT '发放优惠券的标识'")
    private String ename;

    @Transient
    private String logId;
}
