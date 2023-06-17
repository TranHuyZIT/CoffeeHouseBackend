package com.tma.coffeehouse.config.MQConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailMessage {
    String messageId;
    String subject;
    String toMail;
    String body;
    Date createdAt;
}

