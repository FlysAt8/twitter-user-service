package com.twitter.userservice.controller;

import com.twitter.userservice.dto.LoginRequest;
import com.twitter.userservice.dto.LoginResponse;
import com.twitter.userservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = sessionService.login(request);
        return ResponseEntity.ok(response);
    }
}