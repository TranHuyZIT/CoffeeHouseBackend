package com.example.demo.Notification;

import com.example.demo.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(exclude = "notifiedTo")
@ToString(exclude = "notifiedTo")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column

    private String subject;
    @Column(length = 10000)
    private String message;
    @Column
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="notification_user",
        joinColumns = @JoinColumn(name = "notification_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> notifiedTo;
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
}
