package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.model.UserStatsEntity;
import com.claudioscagliotti.thesis.repository.UserStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service class for managing user statistics.
 */
@Service
public class UserStatsService {

    private final UserStatsRepository userStatsRepository;

    /**
     * Constructor for UserStatsService.
     *
     * @param userStatsRepository the repository to manage user statistics
     */
    public UserStatsService(UserStatsRepository userStatsRepository) {
        this.userStatsRepository = userStatsRepository;
    }

    /**
     * Adds statistics for the given user based on the genres of the movies they have watched.
     *
     * @param user  the user entity for which the statistics are being recorded
     * @param genres the list of genres associated with the movies watched by the user
     */
    @Transactional
    public void addUserStats(UserEntity user, List<GenreEntity> genres) {
        for (GenreEntity genre : genres) {
            UserStatsEntity userStats = new UserStatsEntity();
            userStats.setUser(user);
            userStats.setGenre(genre);
            userStatsRepository.save(userStats);
        }
    }

    /**
     * Retrieves a map with genre IDs as keys and their occurrence counts as values for a given user.
     *
     * @param userId the ID of the user
     * @return a map where the key is the genre ID and the value is the count of occurrences
     */
    public Map<Long, Long> getGenreCountByUserId(Long userId) {
        List<Object[]> results = userStatsRepository.findGenreCountByUserId(userId);
        Map<Long, Long> genreCountMap = new HashMap<>();

        for (Object[] result : results) {
            Long genreId = (Long) result[0];
            Long count = (Long) result[1];
            genreCountMap.put(genreId, count);
        }

        return genreCountMap;
    }
}

