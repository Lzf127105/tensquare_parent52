package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.BaseClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Lzf
 * @create 2019-01-12 17:44
 * @Description: 调用tensquare-base
 * /**
 * 问答类
 * 调用其他模块里面的方法（步骤）：
 * 1、导入openfeign的jar包
 * 2、BaseClient的接口名、参数名要一致
 * 3、启动类加入@EnableDiscoveryClient和@EnableFeignClients
 * 4、controller加入private BaseClient baseClient,路由要一致
 */
@FeignClient(value = "tensquare-base", fallback = BaseClientImpl.class)
public interface BaseClient {
    /**
     * 调用服务这边参数名要一致
     */
    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
