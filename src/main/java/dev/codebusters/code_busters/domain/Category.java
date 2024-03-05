package dev.codebusters.code_busters.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Categories")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Category {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime added;

    @Column(nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean exposed;

    @OneToMany(mappedBy = "category")
    private Set<Challenge> categoryChallenges;

}
