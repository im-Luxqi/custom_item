package com.duomai.project.product.general.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 签到,日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_task_sign_log")
@org.hibernate.annotations.Table(appliesTo = "sys_task_sign_log", comment = "签到日志表")
public class SysTaskSignLog  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(100) COMMENT '粉丝混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;


    @Column(columnDefinition = "varchar(10) COMMENT '签到时间，yyyy-MM-dd格式'")
    private String signTime;
    @Column(columnDefinition = "int(11) COMMENT '累计签到'")
    private Integer totalNum;
    @Column(columnDefinition = "int(11) COMMENT '连续签到天数'")
    private Integer continuousNum;


}
