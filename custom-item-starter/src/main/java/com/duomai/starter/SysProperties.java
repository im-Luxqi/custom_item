package com.duomai.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = SysProperties.SYS_PREFIX)
public class SysProperties {

    public static final String SYS_PREFIX = "sys";

    /*
     * 系统
     **/
    @NestedConfigurationProperty
    private SysConfig sysConfig;
    /*
     * 监控
     **/
    @NestedConfigurationProperty
    private DruidConfig druidConfig;
    /*
     * 客户
     **/
    @NestedConfigurationProperty
    private CustomConfig customConfig;
    /*
     * 是否开启表自动生成
     **/
    boolean genFlag;
}
