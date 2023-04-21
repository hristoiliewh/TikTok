package com.tiktok.tiktok.model.repositories;

import com.tiktok.tiktok.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> getByEmail(String email);
    Optional<User> getByUsername(String username);
    Optional<User> getByConfirmationCode(String confirmationCode);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByConfirmationCodeAndId(String confirmationCode, int id);
}
