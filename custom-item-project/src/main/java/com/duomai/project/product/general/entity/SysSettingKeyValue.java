package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 配置表
 */
@Data
@Entity
@Table(name = "sys_setting_key_value")
@org.hibernate.annotations.Table(appliesTo = "sys_setting_key_value", comment = "配置表")
public class SysSettingKeyValue {
    @Id
    @Column(columnDefinition = "varchar(20) COMMENT '编号'")
    private String k;
    @Column(columnDefinition = "text COMMENT '值'")
    private String v;
    @Column(columnDefinition = "varchar(30) COMMENT '类'")
    private String type;

    @Column(columnDefinition = "varchar(50) COMMENT '说明'")
    private String remark;
}
