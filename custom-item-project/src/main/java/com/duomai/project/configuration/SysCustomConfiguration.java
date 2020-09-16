package com.duomai.project.configuration;

import com.duomai.project.configuration.rename.RenamingProcessor;
import com.taobao.api.DefaultTaobaoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 项目SpringBoot相关配置
 */
@Configuration
@EnableConfigurationProperties(SysCustomProperties.class)
public class SysCustomConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        RenamingProcessor renameResolver = new RenamingProcessor(true);
        argumentResolvers.add(renameResolver);
    }


    @Bean
    public DefaultTaobaoClient connectionSettings(SysCustomProperties sysCustomProperties) {
        DefaultTaobaoClient defaultTaobaoClient = new DefaultTaobaoClient(
                "http://gw.api.taobao.com/router/rest",
                sysCustomProperties.getCustomConfig().getAppkey(),
                sysCustomProperties.getCustomConfig().getSecretkey());
        return defaultTaobaoClient;
    }
}