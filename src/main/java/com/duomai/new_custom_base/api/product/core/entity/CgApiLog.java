package com.duomai.new_custom_base.api.product.core.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 记录各个页面的pv
 */
@Data
@Entity
@Table(name = "cg_api_log")
@org.hibernate.annotations.Table(appliesTo = "cg_api_log", comment = "api日志表")
public class CgApiLog {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 32)
    private String id;
    @Column(nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(32)  COMMENT 'appkey'")
    private String appkey;
    @Column(nullable = false, columnDefinition = "varchar(20)  COMMENT '请求ip'")
    private String requestIp;
    @Column(nullable = false, columnDefinition = "varchar(255)  COMMENT '请求方法名'")
    private String apiName;
    @Column(nullable = false, columnDefinition = "text  COMMENT '签名'")
    private String apiSign;

    @Column(nullable = false, columnDefinition = "int(1) COMMENT '类型 0：正常信息 1:异常信息'")
    private Integer parType;
    @Column(nullable = false, columnDefinition = "text COMMENT '请求数据'")
    private String requestData;
    @Column(nullable = false, columnDefinition = "text COMMENT '返回数据'")
    private String responseData;
    @Column(columnDefinition = "text COMMENT '错误信息'")
    private String errorMsg;

}
