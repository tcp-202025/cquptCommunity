package cn.cquptCommunity.sms.listener;

import cn.cquptCommunity.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息队列中的监听器: 用户取消参加活动时发送消息告知用户
 */

@Component
@RabbitListener(queues = "sms_cancel") //rabbit中的监听器(消费方)，用于处理sms_cancel消息队列中的消息
public class Sms_cancelListener {

    @Autowired
    private SmsUtil smsUtil;
    @RabbitHandler //定义处理消息方法
    public void executeSms(Map<String,String> map){
        //拿到消息队列中的信息
        String mobile = map.get("mobile");//手机号
        String gatheringId = map.get("gatheringId");//活动id
        //发送短信给用户
        try {
            smsUtil.sendSms3(mobile,gatheringId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
