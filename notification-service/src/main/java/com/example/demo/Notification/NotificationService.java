package com.example.demo.Notification;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Notification.DTO.AddNotificationDTO;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    public Notification addNotification(AddNotificationDTO addNotificationDTO){
        Notification notification = Notification.builder()
                .image(addNotificationDTO.getImage())
                .subject(addNotificationDTO.getSubject())
                .message(addNotificationDTO.getMessage())
                .build();
        List<User> notifiedTo = new ArrayList<>();
        for (Long userId: addNotificationDTO.getUserIds()) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.getNotifications().add(notification);
                notifiedTo.add(user);
            }
        }
        notification.setNotifiedTo(notifiedTo);
        System.out.println(notification.getNotifiedTo());
        return notificationRepository.save(notification);
    }
    public Page<Notification> getAllNotification
            (Long userId, Integer pageNo, Integer pageSize, String sortBy, boolean reverse){
        Pageable pageable = PageRequest.of(pageNo, pageSize,  Sort.by(reverse ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        return notificationRepository.findAllByUser(userId, pageable);
    }
    public void removeNotification(){
        // Use for cron-jobs to reduce notification storage
    }
}
