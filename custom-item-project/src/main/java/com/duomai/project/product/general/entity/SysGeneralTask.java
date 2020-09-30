package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.TaskTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 入会,关注,签到,日志表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_general_task")
@org.hibernate.annotations.Table(appliesTo = "sys_general_task", comment = "入会,关注,签到,日志表")
public class SysGeneralTask {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 64)
    private String id;
    @Column(columnDefinition = "varchar(47) COMMENT '粉丝混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) COMMENT '任务类型'")
    private TaskTypeEnum taskType;

}
