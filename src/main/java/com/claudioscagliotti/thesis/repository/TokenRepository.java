package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("""
            SELECT t
            FROM TokenEntity t
            INNER JOIN t.user u
            WHERE u.id = :userId AND t.loggedOut = false
            """)
    List<TokenEntity> findAllAccessTokensByUser(@Param("userId") Long userId);
    Optional<TokenEntity> findByAccessToken(String token);
    Optional<TokenEntity> findByRefreshToken(String token);
    Optional<TokenEntity> findByResetPasswordToken(String token);
}
