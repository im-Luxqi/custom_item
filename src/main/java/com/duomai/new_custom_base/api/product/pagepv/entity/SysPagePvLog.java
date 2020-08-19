package com.duomai.new_custom_base.api.product.pagepv.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录各个页面的pv
 */
@Data
@Entity
@Table(name = "sys_page_pv_log")
@org.hibernate.annotations.Table(appliesTo = "sys_page_pv_log", comment = "各个页面的pv")
public class SysPagePvLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 32)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;

    @Column(nullable = false, columnDefinition = "varchar(47) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '具体的页面标识'")
    private String page;

}
