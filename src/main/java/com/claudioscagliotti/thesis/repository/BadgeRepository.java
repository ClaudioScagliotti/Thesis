package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.BadgeEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {
    public List<BadgeEntity> findByUserEntityListContaining(UserEntity userEntity);
}
