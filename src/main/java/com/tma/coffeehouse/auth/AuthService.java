package com.tma.coffeehouse.auth;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.config.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    public AuthenticateResponse register(RegisterRequest request){
        String requestUsername = request.getUserName();
        Optional<User> existUser = userRepository.findByUserName(requestUsername);
        if (existUser.isPresent()) throw new CustomException("Username đã tồn tại", HttpStatus.CONFLICT);
        User user = User.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .phone((request.getPhone()))
                .gender(request.getGender())
                .build();
        userRepository.save(user);
        String token = jwtService.signToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(()->new CustomException("Tài khoản " + request.getUserName()+ " không tồn tại", HttpStatus.NOT_FOUND));
        String token = jwtService.signToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .build();

    }

    public User getIdentity(String token){
        String userName = jwtService.extractUserName(token);
        User user = userRepository.findByUserName(userName).
                orElseThrow(() -> new CustomException("Không tìm thấy tài khoản với username là " + userName , HttpStatus.NOT_FOUND));
        return user;
    }
}
