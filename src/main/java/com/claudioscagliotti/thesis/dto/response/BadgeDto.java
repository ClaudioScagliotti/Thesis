package com.claudioscagliotti.thesis.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BadgeDto {

    private Long id;

    private String name;
    @JsonIgnore
    private Long genreToUnlock;

    private Float level;
}
