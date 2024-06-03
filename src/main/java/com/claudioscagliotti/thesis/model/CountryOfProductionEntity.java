package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "country_of_production")
public class CountryOfProductionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "country_code", length = 5)//iso_3166_1
    private String countryCode;

    public CountryOfProductionEntity(String countryCode) {
        this.countryCode = countryCode;
    }
}
