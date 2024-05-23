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
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    public Course course;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "content", columnDefinition = "TEXT")
    public String content;

    @Column(name = "type", length = 50)
    public String type;

    @Column(name = "total_cards", nullable = false)
    public int totalCards;
}
