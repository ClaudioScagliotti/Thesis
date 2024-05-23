package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id", unique = true)
    public Goal goal;

    @Column(name = "first_name", length = 100, nullable = false)
    public String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    public String lastName;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    public String username;

    @Column(name = "points", nullable = false)
    public int points;

    @Column(name = "streak", nullable = false)
    public int streak;

    @Column(name = "age", nullable = false)
    public int age;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime creationDate;
}
