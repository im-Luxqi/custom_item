package com.duomai.project;

import com.alibaba.fastjson.JSON;
import com.duomai.common.SysProperties;
import com.duomai.project.tool.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.duomai.*.*")
public class CustomItemProjectApplication {


    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(CustomItemProjectApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (path == null || "null".equals(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "启动成功!  URLs:\n\t" +
                "生成实体类: \thttp://localhost:" + port + path + "/tool/gen/table\n\t" +
                "SQL监控: \thttp://" + ip + ":" + port + path + "/druid\t(root/root)\n\t" +
                "----------------------------------------------------------");
        log.info(JSON.toJSONString(ApplicationUtils.getBean(SysProperties.class)));
    }

}
