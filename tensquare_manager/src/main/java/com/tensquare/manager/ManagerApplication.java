package com.tensquare.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * @author Lzf
 * @create 2019-01-15 17:13
 * @Description: 解决多端口，多认证，难重构，跨域等问题（所以设置网关：所有的外部请求都会先经过微服务网关）
 * Zuul路由转发（大致步骤）：
 * 1、引入eureka-client 和zuul的jar包
 * 2、application.yml加入配置信息
 * 3、启动类加入@EnableEurekaClient @EnableZuulProxy
 * 4、调用例如：http://localhost:9011/base/label，因为配置了/base/** #配置请求URL的请求规则
 *      会自动跳转到tensquare-base下的方法里面去
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
