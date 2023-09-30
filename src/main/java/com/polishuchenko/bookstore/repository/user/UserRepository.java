package com.polishuchenko.bookstore.repository.user;

import com.polishuchenko.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User u LEFT JOIN FETCH u.roles where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("FROM User u LEFT JOIN FETCH u.roles where u.firstName = :username")
    User findUserByUsername(String username);
}
