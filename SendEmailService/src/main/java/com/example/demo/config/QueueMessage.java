package com.example.demo.config;

import lombok.Data;

import java.util.Date;

@Data
public class QueueMessage {
    String messageId;
    String subject;
    String toMail;
    String body;
    Date createdAt;
}
