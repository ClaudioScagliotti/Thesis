package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.BadgeEntity;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {
    List<BadgeEntity> findByUserEntityListContaining(UserEntity userEntity);
    @Query("SELECT COUNT(b) FROM BadgeEntity b JOIN b.userEntityList u WHERE u.id = :userId")
    Integer countByUserId(@Param("userId") Long userId);
    List<BadgeEntity> findByGenreToUnlock(GenreEntity genreEntity);
}
