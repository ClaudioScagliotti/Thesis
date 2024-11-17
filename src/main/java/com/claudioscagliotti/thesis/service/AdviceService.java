package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.exception.NoAdviceAvailableException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface AdviceService {

    /**
     * Retrieves the next advice for the specified user.
     *
     * @param username The username of the user.
     * @return The next AdviceDto for the user.
     * @throws NoAdviceAvailableException If no uncompleted advices are available for the user.
     */
    AdviceDto getNextAdvice(String username);

    /**
     * Creates a list of advice for the specified user based on their goals.
     *
     * @param username The username of the user.
     * @return The list of created AdviceDto instances.
     */
    List<AdviceDto> createAdviceList(String username);

    /**
     * Marks the specified advice as completed for the user.
     *
     * @param username The username of the user.
     * @param adviceId The ID of the advice to complete.
     * @return The completed AdviceDto.
     * @throws EntityNotFoundException   If no advice with the specified ID exists.
     * @throws UnauthorizedUserException If the user is not authorized to complete the advice.
     */
    AdviceDto completeAdvice(String username, Long adviceId);

    /**
     * Skips the specified advice for the user.
     *
     * @param username The username of the user.
     * @param adviceId The ID of the advice to skip.
     * @return The skipped AdviceDto.
     * @throws EntityNotFoundException   If no advice with the specified ID exists.
     * @throws UnauthorizedUserException If the user is not authorized to skip the advice.
     */
    AdviceDto skipAdvice(String username, Long adviceId);

    /**
     * Retrieves the AdviceEntity with the specified ID.
     *
     * @param adviceId The ID of the advice to retrieve.
     * @return The retrieved AdviceEntity.
     * @throws EntityNotFoundException If no advice with the specified ID exists.
     */
    AdviceEntity getById(Long adviceId);
}
