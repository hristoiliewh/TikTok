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
    @Query(value = "SELECT * FROM users " +
            "WHERE username LIKE %:username% ", nativeQuery = true)
    Page<User> findAllByUsername(String username, Pageable pageable);
    Optional<User> getByConfirmationCode(String confirmationCode);
    @Query(value = "SELECT * FROM users " +
                "JOIN users_follow_users ON users.id = follower_id " +
                "WHERE following_id = :userId", nativeQuery = true)
    Page<User> findAllByFollowers(int userId, Pageable pageable);
    @Query(value = "SELECT * FROM users " +
            "JOIN users_follow_users ON users.id = following_id " +
            "WHERE follower_id = :userId", nativeQuery = true)
    Page<User> findAllByFollowing(int userId, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByConfirmationCodeAndId(String confirmationCode, int id);
}
