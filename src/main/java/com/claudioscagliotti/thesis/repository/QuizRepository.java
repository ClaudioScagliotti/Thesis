package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

    List<QuizEntity> findAllByLessonId(Long lessonId);

    @Query("SELECT q FROM QuizEntity q " +
            "LEFT JOIN FETCH MultipleChoiceQuizEntity mcq ON q.id = mcq.id " +
            "LEFT JOIN FETCH TrueFalseQuizEntity tfq ON q.id = tfq.id " +
            "LEFT JOIN FETCH ChronologicalOrderQuizEntity coq ON q.id = coq.id " +
            "WHERE q.advice.id = :adviceId")
    List<QuizEntity> findAllByAdviceIdWithFetch(@Param("adviceId") Long adviceId);

    @Query("SELECT q FROM QuizEntity q " +
            "LEFT JOIN FETCH MultipleChoiceQuizEntity mcq ON q.id = mcq.id " +
            "LEFT JOIN FETCH TrueFalseQuizEntity tfq ON q.id = tfq.id " +
            "LEFT JOIN FETCH ChronologicalOrderQuizEntity coq ON q.id = coq.id " +
            "WHERE q.lesson.id = :lessonId")
    List<QuizEntity> findAllByLessonIdWithFetch(@Param("lessonId") Long adviceId);

}
