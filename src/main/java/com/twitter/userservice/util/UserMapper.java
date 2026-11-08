package com.twitter.userservice.util;

import org.springframework.stereotype.Component;

import com.twitter.userservice.dto.UserResponse;
import com.twitter.userservice.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
