package com.example.demo.Notification.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class AddNotificationDTO {
    private String subject;
    private String message;
    private String image;
    private List<Long> userIds;

}
