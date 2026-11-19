package com.twitter.userservice.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(max = 50, message = "Имя не должно превышать 50 символов")
    private String firstname;

    @Size(max = 50, message = "Фамилия не должна превышать 50 символов")
    private String lastname;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Телефон должен содержать от 10 до 15 цифр (можно с + в начале)")
    private String phone;
}