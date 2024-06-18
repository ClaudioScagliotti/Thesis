package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.AdviceStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdviceRepository extends JpaRepository<AdviceEntity, Long> {
    @Query("SELECT a FROM AdviceEntity a " +
            "JOIN a.userEntity u " +
            "WHERE u.username = :username " +
            "AND a.status = 'UNCOMPLETED' " +
            "ORDER BY a.id ASC")
    List<AdviceEntity> findUncompletedAdviceByUsername(@Param("username") String username, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE AdviceEntity a SET a.deadline = :newDeadline WHERE a.id = :adviceId")
    void updateAdviceDeadline(@Param("adviceId") Long adviceId, @Param("newDeadline") LocalDateTime newDeadline);

    @Modifying
    @Transactional
    @Query("UPDATE AdviceEntity a SET a.status = :status WHERE a.id = :adviceId")
    void updateStatus(@Param("adviceId")Long adviceId, @Param("status") AdviceStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE AdviceEntity a SET a.quizResult = :quizResult WHERE a.id = :adviceId")
    void updateQuizResult(@Param("adviceId")Long adviceId, @Param("quizResult") QuizResultEnum quizResult);



}
