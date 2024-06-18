package com.claudioscagliotti.thesis.model;

import com.claudioscagliotti.thesis.enumeration.AdviceStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "advice")
public class AdviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private AdviceStatusEnum status;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "points", nullable = false)
    private Integer points;
    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_result", length = 50)
    private QuizResultEnum quizResult; //condizione necessaria ma non sufficiente affinchè l'advice sia in status SUCCEEDED

    @OneToMany(mappedBy = "advice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizEntity> quizzes;
}
