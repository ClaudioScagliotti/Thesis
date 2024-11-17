package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.GoalDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.model.GoalEntity;

public interface GoalService {
    /**
     * Creates a new goal based on the provided GoalDto and associates it with the user.
     *
     * @param request  The GoalDto containing goal details.
     * @param username The username of the user to associate the goal with.
     * @return The created GoalDto.
     */
    GoalDto createGoal(GoalDto request, String username);

    /**
     * Retrieves the goal associated with the specified user.
     *
     * @param username The username of the user.
     * @return The GoalDto associated with the user.
     */
    GoalDto getGoalByUser(String username);

    /**
     * Composes query parameters for API requests based on the provided goal entity.
     *
     * @param goalEntity The goal entity containing criteria for API queries.
     * @return A string of concatenated query parameters.
     */
    String composeParams(GoalEntity goalEntity);

    /**
     * Saves a goal entity including associated entities such as countries of production, genres, and keywords.
     *
     * @param dto The GoalDto containing data to save.
     * @return The saved GoalEntity.
     */
    GoalEntity saveGoal(GoalDto dto);

    /**
     * Updates an existing goal entity in the database.
     *
     * @param entity The updated GoalEntity to save.
     * @return The updated GoalEntity.
     */
    GoalEntity updateGoal(GoalEntity entity);

    /**
     * Updates the page number of a goal entity based on the response from an external API.
     *
     * @param goalEntity The goal entity to update.
     * @param response   The MovieResponse containing API response data.
     */
    void updatePage(GoalEntity goalEntity, MovieResponse response);
}
