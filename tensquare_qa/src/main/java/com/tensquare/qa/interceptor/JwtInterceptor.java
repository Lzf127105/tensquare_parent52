package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lzf
 * @create 2019-01-12 9:37
 * @Description:
 * 方法一：extends HandlerInterceptorAdapter 
 * 方法二：implements org.springframework.web.servlet.HandlerInterceptor
 */
@Component
@SuppressWarnings("all")
public class JwtInterceptor implements org.springframework.web.servlet.HandlerInterceptor{

    @Autowired
    private JwtUtil jwtUtil;

    //固定写法，操作之前进行拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器（全部都要经过）");
        //无论如何都放行，具体能不能操作还是在具体的操作中去判断
        //拦截器只是负责把头请求中包含token的令牌进行一个解析验证
        String header = request.getHeader("Authorization");//获取请求头信息
        if(header != null && !"".equals(header)){
            System.out.println("header中存在Authorization，开始拦截");
            if(header.startsWith("Bearer ")){  //前后端在header约定的,startsWith获取的是Bearer （固定写法）
                String token = header.substring(7);//提取token
                try {
                    //对令牌进行验证
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    //可以放到request域中，前端可以在request的域中获取判断
                    if (roles != null && roles.equals("admin")){
                        request.setAttribute("claims_admin",token);//如果claims_admin不等于空，就是管理员
                    }
                    if (roles != null && roles.equals("user")){
                        request.setAttribute("claims_user",token);//如果claims_user不等于空，就是管理员
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确！");
                }
            }
        }
        return true;
    }
}
