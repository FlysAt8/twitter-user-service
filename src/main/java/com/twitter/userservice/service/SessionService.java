package com.twitter.userservice.service;

import com.twitter.userservice.dto.LoginRequest;
import com.twitter.userservice.dto.LoginResponse;
import com.twitter.userservice.entity.User;
import com.twitter.userservice.exceptions.UnauthorizedException;
import com.twitter.userservice.repository.UserRepository;
import com.twitter.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Неверные учётные данные"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Неверные учётные данные");
        }

        String token = jwtUtil.generateToken(user.getId());

        return LoginResponse.builder()
                .userId(user.getId())
                .token(token)
                .build();
    }
}