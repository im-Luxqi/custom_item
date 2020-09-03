package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.PvChannelEnum;
import com.duomai.project.product.general.enums.PvPageEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录各个页面的pv
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_page_pv_log")
@org.hibernate.annotations.Table(appliesTo = "sys_page_pv_log", comment = "各个页面的pv")
public class SysPagePvLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 64)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(nullable = false, columnDefinition = "varchar(47) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '具体的页面标识'")
    private PvPageEnum page;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) COMMENT '哪个渠道而来'")
    private PvChannelEnum channel;
}
