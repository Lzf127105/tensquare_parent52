package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lzf
 * @create 2019-01-16 9:16
 * @Description:  网关过滤器（yml文件路径的）
 * jwt使用步骤：
 *  1、引入tensquare_common
 *  2、yml 加入
 *  jwt:
        config:
            key: itcast
    3、在启动类，@Bean加入到容器
    4、调用private JwtUtil jwtUtil;
 */
@Component
@SuppressWarnings("all")
public class ManagerFilter extends ZuulFilter{

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * "pre"表示在请求前执行
     * "post"表示在请求后执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器的请求顺序，数字越小，表示越优先执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启，true表示开启
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作，return任何object的值，都表示继续执行
     * 不想执行：setSendZuulResponse(false);表示不在继续执行
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("后台经过过滤器了！");
        //经过yml处理统一端口请求，ManagerFilter/WebFilter后，头部信息丢失（Bug）
        //解决方法：在WebFilter实现的run()方法中，进行网站前台的token转发
        //得到request上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = currentContext.getRequest();
        //得到头部信息
        String header = request.getHeader("Authorization");
        //判断是否有头部信息
        if(header != null && !"".equals(header)){
            if(header.startsWith("Bearer ")){  //前后端在header约定的,startsWith获取的是Bearer （固定写法）
                String token = header.substring(7);//提取token
                try {
                    //对令牌进行验证
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(request.getMethod().equals("OPTIONS")){
                        return null;
                    }
                    if(request.getRequestURL().indexOf("login")>0){
                        System.out.println("登陆页面"+request.getRequestURL().toString());
                        return null;
                    }
                    //可以放到request域中，前端可以在request的域中获取判断
                    if (roles != null && roles.equals("admin")){
                        currentContext.addZuulRequestHeader("Authorization",header);//把头部信息继续向下传
                        return null;
                    }
                }catch (Exception e){
                    currentContext.setSendZuulResponse(false);//不在继续执行
                }
            }
        }
        currentContext.setSendZuulResponse(false);//终止执行
        currentContext.setResponseStatusCode(403);//springSecurity内部错误码
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
