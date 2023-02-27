package com.tma.coffeehouse.auth;

import com.tma.coffeehouse.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegisterRequest {
    private String name;
    private String userName;
    private String password;
    private String email;
    private Role role;
    private Boolean gender;
    private  String phone;
}
