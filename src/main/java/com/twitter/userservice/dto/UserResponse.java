package com.twitter.userservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.twitter.userservice.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private LocalDateTime createdAt;

    private List<Role> roles;
}
