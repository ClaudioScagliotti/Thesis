package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    List<LessonEntity> getAllLessonByCourseEntity(CourseEntity courseEntity);

    @Query("SELECT l.id FROM LessonEntity l WHERE l.courseEntity.id = :courseId")
    List<Long> findLessonIdsByCourseId(@Param("courseId") Long courseId);

}
