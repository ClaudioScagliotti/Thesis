package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @Query("SELECT c FROM CourseEntity c JOIN c.goalTypeEntityList g WHERE g = :goalTypeEntity")
    List<CourseEntity> findAllByGoalType(@Param("goalTypeEntity") GoalTypeEntity goalTypeEntity);
}
