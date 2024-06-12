package com.claudioscagliotti.thesis.enumeration;

public enum GoalTypeEnum {
    NOW_PLAYING(1L),
    MOST_POPULAR(2L),
    TOP_RATED(3L),
    DISCOVER(4L);

    private final Long id;

    GoalTypeEnum(Long id) {
        this.id = id;
    }
    public Long getValue() {
        return this.id;
    }
}
