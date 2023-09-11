package com.polishuchenko.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
}
