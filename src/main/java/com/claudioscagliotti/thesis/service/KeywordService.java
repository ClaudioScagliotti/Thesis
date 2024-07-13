package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import com.claudioscagliotti.thesis.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing operations related to keywords.
 */
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final AuthenticationService authenticationService;

    /**
     * Constructs a KeywordService with the specified KeywordRepository.
     *
     * @param keywordRepository     The KeywordRepository to interact with KeywordEntity data.
     * @param authenticationService The AuthenticationService to check the user role
     */
    public KeywordService(KeywordRepository keywordRepository, AuthenticationService authenticationService) {
        this.keywordRepository = keywordRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Saves a list of keyword entities into the database.
     *
     * @param keywordEntityList The list of KeywordEntity objects to be saved.
     * @return The list of saved KeywordEntity objects.
     */
    @Transactional
    public List<KeywordEntity> saveAll(List<KeywordEntity> keywordEntityList) {
        return keywordRepository.saveAll(keywordEntityList);
    }

    /**
     * Check if the user has the ADMIN role.
     */
    public void checkIsAdmin(){
        String adminRole = "ROLE_ADMIN";
        if(!authenticationService.hasRole(adminRole)){
            throw new UnauthorizedUserException("The user role must be: ADMIN");
        }
    }
}
