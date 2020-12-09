package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.PlayPartnerEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 互动日志
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_game_log")
@org.hibernate.annotations.Table(appliesTo = "sys_game_log", comment = "互动日志")
public class SysGameLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(64) COMMENT '粉丝混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "varchar(10) COMMENT '互动时间，yyyy-MM-dd格式'")
    private String createTimeString;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(64) COMMENT '互动对象'")
    private PlayPartnerEnum partner;
}
