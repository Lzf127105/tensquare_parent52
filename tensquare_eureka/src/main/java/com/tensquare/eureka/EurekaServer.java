package com.tensquare.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Lzf
 * @create 2019-01-12 17:09
 * @Description: Eureka服务器
 * Eureka服务器创建步骤：
 * 1、导入服务器jar包  2、加入服务器配置yml  3、启动加入@EnableEurekaServer
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class, args);
    }
}
