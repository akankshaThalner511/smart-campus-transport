package com.ams.repositories;

import com.ams.entity.RefreshToken;
import com.ams.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    // Delete all refresh tokens of a specific user (for one active token per user)
    void deleteByUser(User user);
}
