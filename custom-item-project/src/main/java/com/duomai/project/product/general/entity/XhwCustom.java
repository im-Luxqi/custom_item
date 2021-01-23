package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.FollowWayFromEnum;
import com.duomai.project.product.general.enums.MemberWayFromEnum;
import com.duomai.project.product.general.enums.PlayActionEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 粉丝参与表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_custom")
@org.hibernate.annotations.Table(appliesTo = "xhw_custom", comment = "新华网粉丝")
public class XhwCustom {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;

    @Column(columnDefinition = "varchar(255) COMMENT 'ip'")
    private String ip;
    @Column(nullable = false, columnDefinition = "varchar(64) COMMENT '用户混淆昵称'")
    private String buyerNick;
    @Column(columnDefinition = "varchar(255) COMMENT '群的二维码'")
    private String groupChat;



    @Transient
    private boolean newGuy;

}
