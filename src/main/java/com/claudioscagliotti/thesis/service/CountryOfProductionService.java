package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.model.CountryOfProductionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryOfProductionService {

    /**
     * Generates a string of country codes formatted for use in a query parameter.
     *
     * @param countryEntities List of CountryOfProductionEntity objects containing country codes.
     * @return A string representing country codes formatted as "&with_origin_country=code1|code2|code3..."
     */
    public String getCountryCodesAsString(List<CountryOfProductionEntity> countryEntities) {
        if (countryEntities == null || countryEntities.isEmpty()) {
            throw new IllegalArgumentException("Country entities list must not be null or empty");
        }
        return "&" + QueryParamEnum.WITH_ORIGIN_COUNTRY.getValue() + countryEntities.stream()
                .map(CountryOfProductionEntity::getCountryCode)
                .collect(Collectors.joining("|"));
    }
}
