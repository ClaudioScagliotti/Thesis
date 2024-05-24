package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.CountryOfProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryOfProductionRepository extends JpaRepository<CountryOfProduction, Long> {
    
}
