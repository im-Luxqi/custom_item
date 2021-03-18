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
@Table(name = "sys_task_daily_board")
@org.hibernate.annotations.Table(appliesTo = "sys_task_daily_board", comment = "任务面板")
public class SysTaskDailyBoard  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    @Column(columnDefinition = "varchar(64) COMMENT '粉丝混淆昵称'")
    private String buyerNick;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(columnDefinition = "varchar(64) COMMENT '创建时间String'")
    private String createTimeString;
    @Column(columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;

    @Column(columnDefinition = "int(11) COMMENT '累计签到天数'")
    private Integer signTotalNum;
    @Column(columnDefinition = "int(11) COMMENT '连续签到天数'")
    private Integer signContinuousNum;

    @Column(columnDefinition = "int(1) COMMENT '今日是否签到过'")
    private Integer haveFinishSignToday;

    @Column(columnDefinition = "int(1) COMMENT '是否关注店铺'")
    private Integer haveFinishFollow;
    @Column(columnDefinition = "int(1) COMMENT '是否入会'")
    private Integer haveFinishMember;

    @Column(columnDefinition = "varchar(64) COMMENT '今日分享完成轮数'")
    private String todayFinishShareNum;
    @Column(columnDefinition = "varchar(64) COMMENT '分享进度'")
    private String shareProgress;

    @Column(columnDefinition = "varchar(64)  COMMENT '邀请好友关注店铺进度'")
    private String inviteFollowProgress;

    @Column(columnDefinition = "int(1) COMMENT '今日是否完成浏览任务'")
    private Integer haveFinishBrowseToday;
    @Column(columnDefinition = "varchar(64) COMMENT '浏览宝贝进度'")
    private String browseProgress;

    @Column(columnDefinition = "int(1) COMMENT '今日是否完成直播任务'")
    private Integer haveFinishTvToday;


    @Column(columnDefinition = "varchar(64) COMMENT '每日消费进度'")
    private String spendProgress;

    @Column(columnDefinition = "int(1) COMMENT '今日是否完成消费任务'")
    private Integer haveFinishSpendToday;


}
