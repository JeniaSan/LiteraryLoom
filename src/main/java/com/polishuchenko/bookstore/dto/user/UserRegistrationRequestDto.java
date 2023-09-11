package com.polishuchenko.bookstore.dto.user;

import com.polishuchenko.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
    @NotBlank
    @Length(min = 8, max = 100)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
