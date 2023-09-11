package com.polishuchenko.bookstore.service.user;

import com.polishuchenko.bookstore.dto.user.UserRegistrationRequestDto;
import com.polishuchenko.bookstore.dto.user.UserRegistrationResponseDto;
import com.polishuchenko.bookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
