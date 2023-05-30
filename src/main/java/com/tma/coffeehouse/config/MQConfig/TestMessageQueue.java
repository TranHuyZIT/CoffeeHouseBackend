package com.tma.coffeehouse.config.MQConfig;

import com.tma.coffeehouse.Utils.MessageQueueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class TestMessageQueue {
    private final MessageQueueUtils messageQueueUtils;
    @PostMapping("/notification")
    public void testNotificationQueue(@RequestBody com.example.demo.config.Notification notification){
        messageQueueUtils.pushNotificationQueue(notification);
    }
    @PostMapping("/email")
    public void testSendingEmail(@RequestBody EmailMessage emailMessage){
        messageQueueUtils.pushEmailMessageQueue(emailMessage.getSubject(),
                emailMessage.getToMail(), emailMessage.getBody());
    }
}
