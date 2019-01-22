package com.tensquare.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Lzf
 * @create 2019-01-17 16:40
 * 配置中心使用步骤：(注意先启动配置中心)
 * 1、pom引入jar（spring-cloud-config-server）
 * 2、yml加入git配置信息的地址
 * 3、启动类加入@EnableConfigServer
 * 4、需要用到配置中心的 可以用bootstrap.yml（优先级更高）
 * 5、需要用到配置中心的 pom引入jar（spring-cloud-starter-config）
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class);
    }
}
