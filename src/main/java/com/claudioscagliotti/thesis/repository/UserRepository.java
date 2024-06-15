package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.goalEntity.id = :newGoalId WHERE u.id = :userId")
    void updateUser(@Param("userId") Long userId, @Param("newGoalId") Long newGoalId);

    @Query("SELECT u.goalEntity FROM UserEntity u WHERE u.username = :username")
    GoalEntity getGoalEntityByUsername(@Param("username") String username);
}
