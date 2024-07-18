package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.UserStatsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserStatsRepository extends CrudRepository<UserStatsEntity, Long> {

    /**
     * Finds the genre occurrence count for a given user.
     *
     * @param userId the ID of the user
     * @return a list of Object arrays where each array contains a genre ID and its count
     */
    @Query("SELECT us.genre.id, COUNT(us.genre.id) " +
            "FROM UserStatsEntity us " +
            "WHERE us.user.id = :userId " +
            "GROUP BY us.genre.id")
    List<Object[]> findGenreCountByUserId(Long userId);
}
