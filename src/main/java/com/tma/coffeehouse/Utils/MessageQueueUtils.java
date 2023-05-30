package com.tma.coffeehouse.Utils;

import com.example.demo.config.Notification;
import com.tma.coffeehouse.config.MQConfig.EmailMessage;
import com.tma.coffeehouse.config.MQConfig.MQConfig;
import org.aspectj.weaver.ast.Not;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class MessageQueueUtils {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void pushEmailMessageQueue(String subject, String toMail, String body){
        EmailMessage email = new EmailMessage(UUID.randomUUID().toString(), subject, toMail, body, new Date());
        rabbitTemplate.convertAndSend(MQConfig.DIRECT_EXCHANGE, MQConfig.ROUTING_KEY, email);
    }
    public void pushNotificationQueue(Notification notification){
        rabbitTemplate.convertAndSend(MQConfig.DIRECT_EXCHANGE, MQConfig.NOTIFICATION_ROUTING_KEY, notification);
    }
}
