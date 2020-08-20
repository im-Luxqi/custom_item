package com.duomai.starter.taobao;

import com.duomai.starter.SysProperties;
import com.taobao.api.DefaultTaobaoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SysProperties.class)
public class TaobaoAutoConfiguration {


    @Bean(name = "taoBaoClient")
    @ConditionalOnClass(DefaultTaobaoClient.class)
    public DefaultTaobaoClient connectionSettings(SysProperties sysProperties) {
        DefaultTaobaoClient defaultTaobaoClient = new DefaultTaobaoClient(
                "http://gw.api.taobao.com/router/rest",
                sysProperties.getCustomConfig().getAppkey(),
                sysProperties.getCustomConfig().getSecretkey());
        return defaultTaobaoClient;
    }
}
