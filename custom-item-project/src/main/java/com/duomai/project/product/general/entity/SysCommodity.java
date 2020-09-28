package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(length = 64)
    private String id;
    @Column(columnDefinition = "varchar(47) COMMENT '商品名称'")
    private String name;
    @Column(columnDefinition = "bigint(20) COMMENT '商品id'")
    private Long numId;
    private AwardTypeEnum type;
    @Column(columnDefinition = "varchar(255) COMMENT '商品图片'")
    private String img;
    @Column(columnDefinition = "varchar(64) COMMENT '奖品价格'")
    private String price;
}
