package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 浏览日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_browse_log")
@org.hibernate.annotations.Table(appliesTo = "sys_browse_log", comment = "浏览日志表")
public class SysBrowseLog {
    @Id
    @Column(length = 64)
    private String id;
    @Column(columnDefinition = "varchar(47) COMMENT '粉丝混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "varchar(47) COMMENT '商品id'")
    private Long numId;

}
