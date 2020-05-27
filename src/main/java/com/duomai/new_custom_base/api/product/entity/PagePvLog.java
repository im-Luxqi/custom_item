package com.duomai.new_custom_base.api.product.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录各个页面的pv
 *
 * @description
 * @create by 王星齐
 * @date 2020-05-18 11:19
 */
@Data
@Entity
@Table(name = "page_pv_log")
@org.hibernate.annotations.Table(appliesTo = "page_pv_log", comment = "各个页面的pv")
public class PagePvLog {
    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(length = 64)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;

    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '具体的页面标识'")
    private String page;

}
