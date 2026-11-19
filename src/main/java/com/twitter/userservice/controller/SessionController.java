package com.twitter.userservice.controller;

import com.twitter.userservice.dto.ApiResponse;
import com.twitter.userservice.dto.LoginRequest;
import com.twitter.userservice.dto.LoginResponse;
import com.twitter.userservice.service.SessionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = sessionService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}