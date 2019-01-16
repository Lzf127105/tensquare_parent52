package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lzf
 * @create 2019-01-16 9:16
 * @Description:  网关过滤器（yml文件路径的）
 */
@Component
@SuppressWarnings("all")
public class WebFilter extends ZuulFilter{

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
            //把头部信息继续向下传
            currentContext.addZuulRequestHeader("Authorization",header);
        }
        return null;
    }
}
