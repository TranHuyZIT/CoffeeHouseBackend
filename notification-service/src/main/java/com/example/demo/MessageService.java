package com.example.demo;

import com.example.demo.Notification.DTO.AddNotificationDTO;
import com.example.demo.Notification.NotificationService;
import com.example.demo.config.MQConfig;
import com.example.demo.config.Notification;
import com.example.demo.config.QueueMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Value("spring.mail.username")
    String fromEmail;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void sendEmail(QueueMessage email){
        // Check email

        // Log Email Without Sending
        System.out.println(email);
    }

    @RabbitListener(queues = MQConfig.NOTIFICATION_QUEUE_NAME)
    public void sendNotification(Notification notification){
        // Send notification to client
        System.out.println(notification);
        String receiver = notification.getReceiver();
        AddNotificationDTO addNotificationDTO = AddNotificationDTO.builder()
                .image(notification.getImage())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .build();
        if (Objects.equals(receiver, "*")){
            addNotificationDTO.setUserIds(new ArrayList<>());
            // Send notification to all users
            messagingTemplate.convertAndSend("/all", notification);
        }
        else{
            addNotificationDTO.setUserIds(new ArrayList<>(List.of(Long.parseLong(receiver))));
            System.out.println(addNotificationDTO.getUserIds());
            // Send notification to a specific user.
            messagingTemplate.convertAndSend( "/specific/" + receiver, notification);
        }

        // Save notification to database
        notificationService.addNotification(addNotificationDTO);

    }
}
