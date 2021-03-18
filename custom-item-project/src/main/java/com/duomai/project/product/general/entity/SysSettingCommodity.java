package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_setting_commodity")
@org.hibernate.annotations.Table(appliesTo = "sys_setting_commodity", comment = "商品表")
public class SysSettingCommodity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(64) COMMENT '商品名称'")
    private String name;
    @Column(columnDefinition = "bigint(30) COMMENT '商品id'")
    private Long numId;
    @Column(columnDefinition = "varchar(255) COMMENT '商品图片'")
    private String img;
    @Column(columnDefinition = "varchar(30) COMMENT '奖品价格'")
    private String price;

    /**
     * 今日是否浏览
     */
    @Transient
    private boolean todayHasBrowse;
}
