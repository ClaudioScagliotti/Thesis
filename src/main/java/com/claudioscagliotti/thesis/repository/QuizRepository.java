package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.model.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
    List<QuizEntity> findAllByAdviceId(@Param("adviceId") Long adviceId);
    List<QuizEntity> findAllByLessonId(@Param("lessonId") Long lessonId);

    @Modifying
    @Transactional
    @Query("UPDATE QuizEntity a SET a.status = :resultEnum WHERE a.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("resultEnum") QuizResultEnum resultEnum);

}
