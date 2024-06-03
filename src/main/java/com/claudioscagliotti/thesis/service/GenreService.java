package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GenreService {
    public String createGenreIds(GoalEntity goalEntity) {
        return "&" + QueryParamEnum.WITH_GENRES.getValue() +
                goalEntity.getGenreEntityList().stream()
                        .map(GenreEntity::getName)
                        .map(GenreEnum::getByName)
                        .map(GenreEnum::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
    }
}
