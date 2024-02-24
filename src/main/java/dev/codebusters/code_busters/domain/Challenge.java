package dev.codebusters.code_busters.domain;

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

    @Column
    private String title;

    @Column(name = "description", columnDefinition = "longtext")
    private String description;

    @Column
    private Boolean exposed;

    @Column
    private String flag;

    @Column
    private Integer points;

    @OneToMany(mappedBy = "challenge")
    private Set<Submission> challengeSubmissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
