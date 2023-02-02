package com.example.demo.auth;

import com.example.demo.auth.dto.JWTAuthResponse;
import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.SignupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //login
    @PostMapping(value = {"/login"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }


    //signup
    @PostMapping(value = {"/signup"})
    public ResponseEntity<String> register(@RequestBody SignupDto signupDto){
        String token = authService.signup(signupDto);
        return new ResponseEntity<>(token , HttpStatus.CREATED);
    }
}
