package com.tma.coffeehouse.config.MQConfig;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class QueueMessage {
    String messageId;
    String subject;
    String toMail;
    String body;
    Date createdAt;
}

