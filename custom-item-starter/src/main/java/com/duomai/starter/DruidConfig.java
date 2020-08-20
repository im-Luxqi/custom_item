package com.duomai.starter;

import lombok.Data;

/**
 * @description
 * @create by 王星齐
 * @date 2020-08-19 10:04
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
