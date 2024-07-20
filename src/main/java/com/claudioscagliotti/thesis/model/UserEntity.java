package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "password", length = 120, nullable = false)
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id", unique = true)
    private GoalEntity goalEntity;
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;
    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;
    @Column(name = "points", nullable = false)
    private int points;
    @Column(name = "streak", nullable = false)
    private int streak;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "app_user_badge",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private Set<BadgeEntity> badgeEntitySet;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "app_user_course",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<CourseEntity> courseEntityList;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AdviceEntity> adviceEntityList;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LessonProgressEntity> lessonProgressEntityList;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
