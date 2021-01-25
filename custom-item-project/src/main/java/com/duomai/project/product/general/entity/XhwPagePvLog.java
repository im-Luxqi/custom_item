package com.duomai.project.product.general.entity;

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
@Table(name = "xhw_page_pv_log")
@org.hibernate.annotations.Table(appliesTo = "xhw_page_pv_log", comment = "各个页面的pv")
public class XhwPagePvLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "varchar(64) COMMENT '用户混淆昵称'")
    private String buyerNick;

    @Column(columnDefinition = "varchar(255) COMMENT 'ip'")
    private String ip;
}
