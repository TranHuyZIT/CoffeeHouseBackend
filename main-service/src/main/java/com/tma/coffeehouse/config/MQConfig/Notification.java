package com.tma.coffeehouse.config.MQConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private String subject;
    private String receiver;
    private String message;
    private String image;
}
