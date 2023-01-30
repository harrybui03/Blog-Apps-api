package com.example.demo.auth;

import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.SignupDto;

public interface AuthService {
    String signup(SignupDto signupDto);
    String login(LoginDto loginDto);
}
