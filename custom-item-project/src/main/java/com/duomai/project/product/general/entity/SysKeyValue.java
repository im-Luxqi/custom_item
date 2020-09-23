package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 粉丝参与表
 */
@Data
@Entity
@Table(name = "sys_key_value")
@org.hibernate.annotations.Table(appliesTo = "sys_key_value", comment = "键值对表")
public class SysKeyValue {
    @Id
    @Column(columnDefinition = "varchar(20) COMMENT '编号'")
    private String k;
    @Column(columnDefinition = "text COMMENT '值'")
    private String v;
    @Column(columnDefinition = "varchar(20) COMMENT '类'")
    private String type;
}
