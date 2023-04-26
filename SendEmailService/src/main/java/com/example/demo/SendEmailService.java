package com.example.demo;

import com.example.demo.config.MQConfig;
import com.example.demo.config.QueueMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
    @Value("spring.mail.username")
    String fromEmail;
    @Autowired
    JavaMailSender javaMailSender;
    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void sendEmail(QueueMessage email){
        // Log Email Without Sending
        System.out.println(email);

    }
}
