package com.example.demo.Notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n LEFT JOIN n.notifiedTo u WHERE " +
            "u.id = :userId or SIZE(n.notifiedTo) = 0")
    Page<Notification> findAllByUser(@Param("userId")Long userId, Pageable pageable);
}
