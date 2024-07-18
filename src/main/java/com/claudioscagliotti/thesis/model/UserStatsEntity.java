package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "APP_USER_STATS")
public class UserStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private GenreEntity genre;
}
