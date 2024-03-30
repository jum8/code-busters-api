package dev.codebusters.code_busters.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class VerificationToken {

    private static final int EXPIRATION = 60 * 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser user;

    public VerificationToken(String token, AppUser user) {
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusSeconds(EXPIRATION);
        this.user = user;
    }

    public Boolean isExpired() {
        return this.expiryDate.isBefore(LocalDateTime.now());
    }
}
