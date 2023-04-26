package com.tma.coffeehouse.config.MQConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessagePublisher {
    private final RabbitTemplate template;
    @PostMapping("/api/v1/test-publish")
    public String publishMessage(@RequestBody QueueMessage message){
        message.setMessageId(UUID.randomUUID().toString());
        message.setCreatedAt(new Date());
        template.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, message);
        return "Message sent!";
    }
}
