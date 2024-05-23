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
@Table(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    public User user;

    @Column(name = "recommendation_type", length = 50, nullable = false)
    public String recommendationType;

    @Column(name = "status", length = 50, nullable = false)
    public String status;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime creationDate;

    @Column(name = "deadline")
    public LocalDateTime deadline;

    @Column(name = "points", nullable = false)
    public int points;

    @Column(name = "quiz_result", length = 50)
    public String quizResult;
}
