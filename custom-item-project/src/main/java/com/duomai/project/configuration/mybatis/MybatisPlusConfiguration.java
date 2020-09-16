package com.duomai.project.configuration.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.duomai.common.framework.mybatisplus.MybatisPlusSqlInjector;
import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**   配置mybatis
 * @description
 */
@Configuration
public class MybatisPlusConfiguration {


    /**
     * 自定义注入语句
     *
     * @return
     */
    @Bean
    public MybatisPlusSqlInjector mybatisPlusSqlInjector() {
        return new MybatisPlusSqlInjector();
    }

    /***
     * plus 的性能优化
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        /* <!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 --> */
        //performanceInterceptor.setMaxTime(1000);
        /* <!--SQL是否格式化 默认false--> */
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
