package com.tma.coffeehouse.auth;

import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.User.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticateResponse {
    private String token;
    private String refreshToken;
    private Long id;
    private String name;
    private String userName;
    private  String email;
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
}
