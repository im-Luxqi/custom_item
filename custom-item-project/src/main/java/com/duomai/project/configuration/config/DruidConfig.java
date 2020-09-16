package com.duomai.project.configuration.config;

import lombok.Data;

/** 德鲁伊配置
 * @description
 */
@Data
public class DruidConfig {

    /*
     * 监控账户
     **/
    private String monitorUsername;
    /*
     * 监控密码
     **/
    private String monitorPassword;
}
