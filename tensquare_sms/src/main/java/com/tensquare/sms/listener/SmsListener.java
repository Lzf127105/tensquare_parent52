package com.tensquare.sms.listener;

import com.tensquare.sms.utils.BatianSmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Lzf
 * @create 2019-01-09 14:16
 * @Description: 短信监听类
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    /*@Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name")
    private String sign_name;*/

    /**发送短信*/
    @RabbitHandler
    public void sendSms(Map<String,String> message){
        System.out.println("手机号："+message.get("mobile"));
        System.out.println("验证码："+message.get("checkCode"));
        BatianSmsUtil.sendSMS(message.get("mobile"), "您的十次方短信验证码为："+message.get("checkCode"));
        /*try {
            //没有充值暂时，调用芭田的接口
            smsUtil.sendSms(message.get("mobile"),template_code,sign_name,"{\"number\":\""+ message.get("code")  +"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }*/
    }
}
