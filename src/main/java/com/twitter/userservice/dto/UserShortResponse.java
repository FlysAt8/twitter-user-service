package com.twitter.userservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class UserShortResponse {
    private UUID id;
    private String firstname;
    private String lastname;
}
