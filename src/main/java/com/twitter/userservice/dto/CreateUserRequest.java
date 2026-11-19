package com.twitter.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не должно превышать 50 символов")
    private String firstname;

    @Size(max = 50, message = "Фамилия не должна превышать 50 символов")
    private String lastname;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    private String email;

    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Телефон должен содержать от 10 до 15 цифр (можно с + в начале)")
    private String phone;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    @ToString.Exclude // Чтобы пароль не попадал в логи при выводе объекта
    private String password;
}