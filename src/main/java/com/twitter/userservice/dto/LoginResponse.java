package com.twitter.userservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class LoginResponse {
    private UUID userId;
    private String token;
}
