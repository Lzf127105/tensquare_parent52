package com.tensquare.qa.config;

import com.tensquare.qa.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Lzf
 * @create 2019-01-12 9:57
 * @Description:
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired    
    private JwtInterceptor jwtInterceptor;

    @Override    
    public void addInterceptors(InterceptorRegistry registry){
        //注册拦截器要声明拦截器对象 和 要拦截的请求
        System.out.println("放行规则");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**");
    }
}
