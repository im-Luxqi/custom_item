package com.duomai.project.product.general.entity;

import com.duomai.project.product.general.enums.AwardRunningEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 奖品表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "xhw_award")
@org.hibernate.annotations.Table(appliesTo = "xhw_award", comment = "奖品表")
public class XhwAward implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;


    @Column(columnDefinition = "varchar(64) COMMENT '奖品名称'")
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '奖品图片'")
    private String img;

    @Column(columnDefinition = "varchar(255) COMMENT '奖品获奖弹窗图片'")
    private String winImg;


    @Column(columnDefinition = "int(10) COMMENT '奖品展示数量'")
    private Integer showNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品总数量'")
    private Integer totalNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品剩余数量'")
    private Integer remainNum;
    @Column(columnDefinition = "int(10) COMMENT '奖品已发数量'")
    private Integer sendNum;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30) COMMENT '奖品发放状态'")
    private AwardRunningEnum awardRunningType;

    @Column(columnDefinition = "int(10) COMMENT '优先级'")
    private Integer level;

    @Column(columnDefinition = "int(1) COMMENT '开抢'")
    private Integer canRob;

//
//    @Column(columnDefinition = "DATETIME COMMENT '开枪时间'")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
//    private Date drawStartTime;

    @Transient
    private String logId;
    @Transient
    private boolean hasGet;

}
