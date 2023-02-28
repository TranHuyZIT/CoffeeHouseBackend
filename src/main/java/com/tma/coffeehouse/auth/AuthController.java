package com.tma.coffeehouse.auth;


import com.tma.coffeehouse.User.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST, path="/register")
    public ResponseEntity<AuthenticateResponse> register(@RequestBody RegisterRequest request){
        AuthenticateResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path="/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request){
        AuthenticateResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/identity")
    public User getIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer", "");
        User response = authService.getIdentity(token);
        return response;
    }
}
