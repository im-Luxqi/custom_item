package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 商品表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_commodity")
@org.hibernate.annotations.Table(appliesTo = "sys_commodity", comment = "商品表")
public class SysCommodity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 64)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "varchar(47) COMMENT '商品名称'")
    private String name;
    @Column(columnDefinition = "bigint(20) COMMENT '商品id'")
    private Long numId;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) COMMENT '商品类型 GOODS 商品 COUPON 优惠券'")
    private AwardTypeEnum type;
    @Column(columnDefinition = "varchar(255) COMMENT '商品图片'")
    private String img;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品价格'")
    private String price;
    @Column(columnDefinition = "varchar(64) COMMENT '分类字段 total 主商品 everyDay 每天商品'")
    private String commoditySort;

    //是否浏览 0 未浏览 1 已浏览
    @Transient
    private Integer isBrowse = 0;

    //商品名称组合
    @Transient
    private String[] names;
}
