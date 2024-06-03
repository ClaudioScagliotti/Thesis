package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryOfProductionDto {
    private Long id;
    private String name;
    private String countryCode;
}
