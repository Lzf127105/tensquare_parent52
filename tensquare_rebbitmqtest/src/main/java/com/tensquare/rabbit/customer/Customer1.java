package com.tensquare.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Lzf
 * @create 2019-01-04 16:25
 * @Description:
 */
@Component
@RabbitListener(queues = "itcast")
public class Customer1 {

    @RabbitHandler
    public void getMsg(String msg){
        System.out.println("直接模式消费信息："+msg);
    }
}
