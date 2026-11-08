package com.twitter.userservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
}
