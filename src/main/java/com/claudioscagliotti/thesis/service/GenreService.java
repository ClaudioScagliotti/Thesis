package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public String createGenreIds(GoalEntity goalEntity) {
        return "&" + QueryParamEnum.WITH_GENRES.getValue() +
                goalEntity.getGenreEntityList().stream()
                        .map(GenreEntity::getName)
                        .map(GenreEnum::getByName)
                        .map(GenreEnum::getTmdbId)
                        .map(Object::toString)
                        .collect(Collectors.joining("|"));
    }
    @Transactional
    GenreEntity getGenreByNameAndSaveIfNotExists(GenreEntity entity){
        GenreEntity genreEntityByName = genreRepository.getGenreEntityByName(entity.getName());
        if(genreEntityByName==null){
            return genreRepository.save(entity);
        }
        return genreEntityByName;
    }


    public List<GenreEntity> mapGenreIdsToEntities(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return null;
        }
        return genreIds.stream()
                .map(genreRepository::getGenreEntityByTmdbId)
                .collect(Collectors.toList());
    }

    public List<Integer> mapGenreEntitiesToIds(List<GenreEntity> genreEntities) {
        if (genreEntities == null || genreEntities.isEmpty()) {
            return null;
        }
        return genreEntities.stream()
                .map(GenreEntity::getTmdbId)
                .collect(Collectors.toList());
    }

}
