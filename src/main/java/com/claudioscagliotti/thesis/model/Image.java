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
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "url",  nullable = false)
    public String url;

    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Column(name = "base64_data", columnDefinition = "TEXT")
    public String base64Data;
}
