package com.example.demo.User;

import com.example.demo.Notification.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
@Setter
@Getter
@EqualsAndHashCode(exclude = "notifications")
@ToString(exclude = "notifications")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)

    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "notifiedTo", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column
    Timestamp updatedAt;

}
