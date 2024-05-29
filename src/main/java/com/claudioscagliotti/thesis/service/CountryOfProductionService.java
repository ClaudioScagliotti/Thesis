package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.model.CountryOfProductionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryOfProductionService {
    public String getCountryCodesAsString(List<CountryOfProductionEntity> countryEntities) {
        return "&" + QueryParamEnum.WITH_ORIGIN_COUNTRY.getValue() + countryEntities.stream()
                .map(CountryOfProductionEntity::getCountryCode)
                .collect(Collectors.joining(","));
    }
}
