package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.goalEntity.id = :newGoalId WHERE u.id = :userId")
    void updateUserGoal(@Param("userId") Long userId, @Param("newGoalId") Long newGoalId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM app_user_course WHERE app_user_id = :userId", nativeQuery = true)
    void deleteUserCourses(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO app_user_course (app_user_id, course_id) VALUES (:userId, :courseId)", nativeQuery = true)
    void addUserCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    @Query("SELECT u.goalEntity FROM UserEntity u WHERE u.username = :username")
    GoalEntity getGoalEntityByUsername(@Param("username") String username);

    List<UserEntity> getAllUserByRole(RoleEnum roleEnum);
}
