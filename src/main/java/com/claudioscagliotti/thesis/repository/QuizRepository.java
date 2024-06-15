package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

    List<QuizEntity> findAllByLessonId(Long lessonId);
    List<QuizEntity> findAllByAdviceId(Long adviceId);

    
}
