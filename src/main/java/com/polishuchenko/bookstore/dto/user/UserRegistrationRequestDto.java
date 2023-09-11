package com.polishuchenko.bookstore.dto.user;

import com.polishuchenko.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@FieldMatch(field = "password", fieldMatch = "repeatPassword")
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Length(min = 8, max = 100)
        String password,
        @NotBlank
        @Length(min = 8, max = 100)
        String repeatPassword,
        @NotBlank
        String firstName,
        @NotBlank String lastName,
        String shippingAddress) {
}
