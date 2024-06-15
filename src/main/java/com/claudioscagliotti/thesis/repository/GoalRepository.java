package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {

    GoalTypeEntity getGoalTypeById(Long goalId);

    @Query("SELECT gt.type FROM GoalEntity g JOIN g.goalType gt WHERE g.id = :id")
    List<GoalTypeEnum> findGoalTypesById(@Param("id") Long id);
    @Query("SELECT gt FROM GoalEntity g JOIN g.goalType gt WHERE g.id = :id")
    List<GoalTypeEntity> findGoalTypesById2(@Param("id") Long id);//TODO REFACTOR




}
