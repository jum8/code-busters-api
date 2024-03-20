package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUser(AppUser user);
    Optional<PasswordResetToken> findByToken(String token);
}
