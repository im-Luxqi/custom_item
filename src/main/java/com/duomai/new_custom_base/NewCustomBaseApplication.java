package com.duomai.new_custom_base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class NewCustomBaseApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(NewCustomBaseApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (path == null || "null".equals(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \thttp://localhost:" + port + path + "/\n\t" +
                "Druid: \thttp://" + ip + ":" + port + path + "/druid\t(admin/admin123)\n\t" +
                "----------------------------------------------------------");
    }

}
