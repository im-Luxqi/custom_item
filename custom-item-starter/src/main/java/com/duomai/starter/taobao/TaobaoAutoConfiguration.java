package com.duomai.starter.taobao;

import com.duomai.starter.SysProperties;
import com.taobao.api.DefaultTaobaoClient;
import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
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
