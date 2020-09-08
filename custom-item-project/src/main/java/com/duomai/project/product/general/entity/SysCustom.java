package com.duomai.project.product.general.entity;

import com.duomai.project.product.demo.enums.Sex;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 粉丝参与表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_custom")
@org.hibernate.annotations.Table(appliesTo = "sys_custom", comment = "粉丝参与表")
public class SysCustom {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;
    @Column(nullable = false, columnDefinition = "varchar(47) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(columnDefinition = "varchar(64) COMMENT '用户openID'")
    private String openId;
    @Column(columnDefinition = "varchar(64) COMMENT '真实昵称'")
    private String znick;
    @Column(columnDefinition = "varchar(255) COMMENT '头像'")
    private String headImg;

    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否为会员 0:否1:是'")
    private Integer member;
    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否老会员 0:否1:是'")
    private Integer oldMember;
    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否粉丝 0:否1:是'")
    private Integer fans;
    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否老粉丝 0:否1:是'")
    private Integer oldFans;
}
