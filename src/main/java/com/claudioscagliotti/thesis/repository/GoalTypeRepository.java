package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalTypeRepository extends JpaRepository<GoalTypeEntity, Long> {

    GoalTypeEntity findGoalTypeEntityByType(GoalTypeEnum goalTypeEnum);
}
