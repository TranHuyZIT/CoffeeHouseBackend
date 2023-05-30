package com.example.demo.Notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping
    public Page<Notification> getNotifications(
            @RequestParam(value = "pageNo", name = "pageNo", defaultValue = "") Integer pageNo,
            @RequestParam(value = "pageSize", name = "pageSize", defaultValue = "") Integer pageSize,
            @RequestParam(value = "sortBy", name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "reverse", name = "reverse", defaultValue = "false") Boolean reverse,
            @RequestParam(value = "userId", name = "userId", defaultValue = "") Long userId
    ) {
        return notificationService.getAllNotification(userId, pageNo, pageSize, sortBy, reverse);
    }
}
