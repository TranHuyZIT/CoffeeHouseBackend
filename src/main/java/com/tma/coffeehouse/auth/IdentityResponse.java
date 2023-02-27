package com.tma.coffeehouse.auth;
import com.tma.coffeehouse.User.Role;
import lombok.*;

@RequiredArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class IdentityResponse {
    private Long id;
    private String userName;
    private String email;
    private String name;
    private Role role;
    private String phone;
    private Boolean gender;
}
