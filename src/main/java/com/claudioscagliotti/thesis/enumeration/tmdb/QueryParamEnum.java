package com.claudioscagliotti.thesis.enumeration.tmdb;

public enum QueryParamEnum {
    ADULT("include_adult="),
    WITH_ORIGIN_COUNTRY("with_origin_country="),
    INCLUDE_ADULT("include_adult="),
    INCLUDE_ALL_MOVIES("include_all_movies="),
    LANGUAGE("language="),
    MOVIE_ID("movie_id="),
    PAGE("page="),
    PRIMARY_RELEASE_DATE_GTE("primary_release_date.gte="),
    PRIMARY_RELEASE_DATE_LTE("primary_release_date.lte="),

    RELEASE_DATE_GTE("release_date.gte="),
    RELEASE_DATE_LTE("release_date.lte="),
    SORT_BY("sort_by="),
    SORT_ORDER("sort_order="),
    WITH_CAST("with_cast="),

    WITH_GENRES("with_genres="),
    WITH_KEYWORDS("with_keywords="),
    WITH_PEOPLE("with_people="),
    YEAR("year=");
    private final String value;

    QueryParamEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}
