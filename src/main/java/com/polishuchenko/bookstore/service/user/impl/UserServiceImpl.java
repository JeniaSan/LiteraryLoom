package com.polishuchenko.bookstore.service.user.impl;

import com.polishuchenko.bookstore.dto.user.UserRegistrationRequestDto;
import com.polishuchenko.bookstore.dto.user.UserRegistrationResponseDto;
import com.polishuchenko.bookstore.exception.RegistrationException;
import com.polishuchenko.bookstore.mapper.UserMapper;
import com.polishuchenko.bookstore.model.Role;
import com.polishuchenko.bookstore.model.User;
import com.polishuchenko.bookstore.repository.role.RoleRepository;
import com.polishuchenko.bookstore.repository.user.UserRepository;
import com.polishuchenko.bookstore.service.shoppingcart.ShoppingCartService;
import com.polishuchenko.bookstore.service.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        Role role = roleRepository.getByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        shoppingCartService.createShoppingCart(savedUser);
        return userMapper.toResponseUser(savedUser);
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
