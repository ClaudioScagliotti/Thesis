package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgressEntity, Long> {

    @Query("SELECT lp FROM LessonProgressEntity lp " +
            "JOIN lp.lessonEntity le " +
            "WHERE lp.userEntity.id = :userId " +
            "AND le.courseEntity.id = :courseId " +
            "AND lp.completedCards < le.totalCards " +
            "ORDER BY le.id ASC")
    Optional<LessonProgressEntity> getNextLessonUncompleted(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT lp FROM LessonProgressEntity lp " +
            "WHERE lp.userEntity.id = :userId " +
            "AND lp.lessonEntity.id = :lessonId " +
            "ORDER BY lp.id ASC")
    Optional<LessonProgressEntity> getLessonProgressByUserIdAndLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
    @Modifying
    @Transactional
    @Query("UPDATE LessonProgressEntity a SET a.progress = :progress, a.completedCards= :completedCards WHERE a.id = :lessonProgressId")
    void updateLessonProgress(@Param("lessonProgressId")Long lessonProgressId, @Param("progress") Float progress, @Param("completedCards") Integer completedCards);

    @Query("SELECT lp FROM LessonProgressEntity lp " +
            "JOIN lp.lessonEntity l " +
            "JOIN l.courseEntity c " +
            "WHERE c.id = :courseId " +
            "AND lp.userEntity.id= :userId "+
            "AND lp.status= 'UNCOMPLETED' "+
            "AND lp.completedCards < l.totalCards " +
            "ORDER BY l.id ASC")
    Optional<LessonProgressEntity> getNextUncompletedLessonProgressByCourseId(@Param("courseId") Long courseId,@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE LessonProgressEntity a SET a.status = :status, a.quizResult= :quizResult WHERE a.id = :lessonProgressId")
    void updateLessonProgressStatusAndQuizResult(@Param("lessonProgressId") Long lessonProgressId, @Param("status") LessonStatusEnum status, @Param("quizResult") QuizResultEnum quizResult);

    @Query("SELECT lp.id FROM LessonProgressEntity lp " +
            "JOIN lp.lessonEntity l " +
            "WHERE l.courseEntity.id = :courseId " +
            "AND lp.userEntity.id = :userId")
    List<Long> findLessonProgressIdsByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

}
