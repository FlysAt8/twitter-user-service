package com.twitter.userservice.util;

import com.twitter.userservice.dto.RoleResponse;
import com.twitter.userservice.dto.UserShortResponse;
import org.springframework.stereotype.Component;

import com.twitter.userservice.dto.UserResponse;
import com.twitter.userservice.entity.User;
import com.twitter.userservice.entity.Role;

import java.util.List;

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

    public UserResponse toResponse(User user, List<Role> roles) {
        UserResponse response = toResponse(user);
        if (response != null && roles != null) {
            response.setRoles(roles.stream()
                    .map(role -> RoleResponse.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .build())
                    .toList());
        }
        return response;
    }

    public UserShortResponse toShortResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserShortResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}