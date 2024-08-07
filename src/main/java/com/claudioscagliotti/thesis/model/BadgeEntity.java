package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "badge")
public class BadgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "badgeEntitySet", fetch = FetchType.EAGER)
    private List<UserEntity> userEntityList;

    @ManyToOne
    @JoinColumn(name = "genre_to_unlock", referencedColumnName = "id")
    private GenreEntity genreToUnlock;

    @Column(name="multiplier_level")
    private Float level;
}
