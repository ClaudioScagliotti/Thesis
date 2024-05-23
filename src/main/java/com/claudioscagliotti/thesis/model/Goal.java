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
@Table(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "time_to_dedicate", nullable = false)
    public int timeToDedicate;

    @Column(name = "goal_type", nullable = false)
    public String goalType;

    @Column(name = "year", nullable = false)
    public int year;

    @Column(name = "deadline")
    public LocalDateTime deadline;

    @Column(name = "points", nullable = false)
    public int points;
}
