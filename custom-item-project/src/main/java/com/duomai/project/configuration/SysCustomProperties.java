package com.duomai.project.configuration;

import com.duomai.project.configuration.config.CustomConfig;
import com.duomai.project.configuration.config.DruidConfig;
import com.duomai.project.configuration.config.OcsConfig;
import com.duomai.project.configuration.config.SysConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Data
@Component
@Primary
@ConfigurationProperties(prefix = SysCustomProperties.SYS_PREFIX)
public class SysCustomProperties {

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
     * 客户
     **/
    @NestedConfigurationProperty
    private OcsConfig ocsConfig;

    /*
     * 是否开启表自动生成
     **/
    boolean genFlag;
}
