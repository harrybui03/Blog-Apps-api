package com.example.demo.auth;

import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.SignupDto;
import com.example.demo.exception.BlogAPIException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.users.Roles;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String signup(SignupDto signupDto)
    {
        // check if username or email already exists
        if(userRepository.existsByUsername(signupDto.getUsername()))
        {
            throw new BlogAPIException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        // check if email already exists
        if(userRepository.existsByEmail(signupDto.getEmail()))
        {
            throw new BlogAPIException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRole(Roles.USER);
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }
}

