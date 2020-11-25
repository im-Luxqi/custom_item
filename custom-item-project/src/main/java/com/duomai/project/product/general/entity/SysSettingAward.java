package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import com.duomai.project.product.general.enums.AwardUseWayEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 奖品表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_setting_award")
@org.hibernate.annotations.Table(appliesTo = "sys_setting_award", comment = "奖品表")
public class SysSettingAward {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '奖品用途'")
    private AwardUseWayEnum useWay;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '奖品类型'")
    private AwardTypeEnum type;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String img;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品价格'")
    private String price;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品描述'")
    private String description;



    @Column(columnDefinition = "int(10) COMMENT '奖品总数量'")
    private Integer totalNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品剩余数量'")
    private Integer remainNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品已发数量'")
    private Integer sendNum;

    @Column(columnDefinition = "varchar(30) COMMENT '奖品概率'")
    private String luckyValue;
    @Column(columnDefinition = "int(2) COMMENT '奖池等级'")
    private Integer poolLevel;
    @Column(columnDefinition = "varchar(64) COMMENT '发放优惠券的标识'")
    private String ename;



    @Transient
    private String logId;
    @Transient
    private Integer haveGetNum;


    /**
     * 奖品数量不足
     */
    @Transient
    private Boolean awardIsNull;


    /**
     * 已达到兑换上限 或者 已中过实物
     */
    @Transient
    private Boolean canNotExchange;



    /**
     * 今日不能兑换
     */
    @Transient
    private Boolean todayCanNotExchange;



    /**
     * 未达到兑换条件
     */
    @Transient
    private Boolean HaveNotGetCondition;




}
