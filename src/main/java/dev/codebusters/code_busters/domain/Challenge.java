package dev.codebusters.code_busters.domain;

import dev.codebusters.code_busters.model.ChallengeLevel;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Challenges")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Challenge {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime added;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String description;

    @Column(nullable = false)
    private Boolean exposed;

    @Column(nullable = false)
    private String flag;

    @Column(nullable = false)
    private Long points;

    @Column(nullable = false)
    private Long credits;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeLevel level;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean premium;

    @OneToMany(mappedBy = "challenge")
    private Set<Submission> challengeSubmissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private Set<Hint> hints;

}
