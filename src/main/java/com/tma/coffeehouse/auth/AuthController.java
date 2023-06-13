package com.tma.coffeehouse.auth;


import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.User.DTO.UserResponseDTO;
import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.User.Role;
import com.tma.coffeehouse.Utils.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final FirebaseService firebaseService;

    @RequestMapping(method = RequestMethod.POST, path="/register")
    public ResponseEntity<AuthenticateResponse> register(@RequestBody RegisterRequest request){
        AuthenticateResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST, path = "/refresh-token")
    public ResponseEntity<AuthenticateResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        String accessToken = authService.requestRefreshToken(bearerToken);
        return new ResponseEntity<>(
                AuthenticateResponse.builder()
                        .token(accessToken)
                        .build(), HttpStatus.CREATED
        );
    }
    @RequestMapping(method = RequestMethod.POST, path = "/firebase/login")
    public ResponseEntity<AuthenticateResponse> loginWithSocial(@RequestHeader(HttpHeaders.AUTHORIZATION) String idToken) throws FirebaseAuthException {
        System.out.println(idToken);
        return new ResponseEntity<>(firebaseService.signinWithSocial(idToken), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path="/login")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request){
        AuthenticateResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/identity")
    public UserResponseDTO getIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer", "");
        UserResponseDTO response = authService.getIdentity(token);
        return response;
    }
}
