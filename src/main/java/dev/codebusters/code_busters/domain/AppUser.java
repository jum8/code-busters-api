package dev.codebusters.code_busters.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AppUser {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String name;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime added;

    @Column
    private OffsetDateTime lastActive;

    @Column(name = "passhash")
    private String password;

    @Column
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    private Set<Submission> userSubmissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    public AppUser() {
        this.enabled = true;
    }
}
