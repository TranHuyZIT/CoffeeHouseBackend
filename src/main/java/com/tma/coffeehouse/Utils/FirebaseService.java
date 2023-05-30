package com.tma.coffeehouse.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.tma.coffeehouse.User.Role;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.auth.AuthService;
import com.tma.coffeehouse.auth.AuthenticateResponse;
import com.tma.coffeehouse.auth.RegisterRequest;
import com.tma.coffeehouse.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final FirebaseAuth firebaseAuth;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public FirebaseToken decodeToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }
    public AuthenticateResponse signinWithSocial(String idToken) throws FirebaseAuthException {
        FirebaseToken firebaseToken = this.decodeToken(idToken);
        Map<String, Object> claims = firebaseToken.getClaims();
        System.out.println(claims);
        String name = (String) claims.get("name");
        String pictureUrl = (String) claims.get("picture");
        String userName = (String) claims.get("user_id");
        Optional<User> existedUser = userRepository.findByUserName(userName);
        if (existedUser.isPresent()){
            // Login this user
            User user = existedUser.get();
            String token = jwtService.signToken(user);
            return AuthenticateResponse.builder()
                    .id(user.getId())
                    .userName(userName)
                    .role(user.getRole())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .gender(user.getGender())
                    .token(token)
                    .build();
        }
        // Register user
        RegisterRequest request = RegisterRequest.builder()
                .email("")
                .userName(userName)
                .password(UUID.randomUUID().toString())
                .role(Role.USER)
                .phone("")
                .name(name)
                .build();
        return authService.createCustomer(request, pictureUrl);
    }
}
