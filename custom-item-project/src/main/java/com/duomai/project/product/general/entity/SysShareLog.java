package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 分享日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_share_log")
@org.hibernate.annotations.Table(appliesTo = "sys_share_log", comment = "分享日志表")
public class SysShareLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 64)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(64) COMMENT '分享人mix'")
    private String mixSharer;

    @Column(columnDefinition = "varchar(255) COMMENT '分享人'")
    private String sharer;

    @Column(columnDefinition = "varchar(255) COMMENT '被分享人头像'")
    private String sharerImg;

    @Column(columnDefinition = "varchar(64) COMMENT '被分享人mix'")
    private String mixShareder;

    @Column(columnDefinition = "varchar(255) COMMENT '被分享人'")
    private String shareder;

    @Column(columnDefinition = "varchar(255) COMMENT '被分享人头像'")
    private String sharederImg;

    @Column(nullable = false, columnDefinition = "int(1)  COMMENT '是否分享成功 0:否1:是'")
    private Integer haveSuccess;
}
