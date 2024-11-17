package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.CountryOfProductionEntity;

import java.util.List;

public interface CountryOfProductionService {

    /**
     * Generates a string of country codes formatted for use in a query parameter.
     *
     * @param countryEntities List of CountryOfProductionEntity objects containing country codes.
     * @return A string representing country codes formatted as "&with_origin_country=code1|code2|code3..."
     * @throws IllegalArgumentException If the countryEntities list is null or empty.
     */
    String getCountryCodesAsString(List<CountryOfProductionEntity> countryEntities);
}
