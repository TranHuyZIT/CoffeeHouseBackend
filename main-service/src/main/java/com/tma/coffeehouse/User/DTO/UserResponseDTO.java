package com.tma.coffeehouse.User.DTO;

import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.User.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String userName;
    private  String email;
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String token = "";
}
