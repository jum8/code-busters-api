package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.PasswordResetToken;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.PasswordResetTokenRepository;
import dev.codebusters.code_busters.util.InvalidTokenException;
import dev.codebusters.code_busters.util.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, AppUserRepository appUserRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(String baseUrl, String email) {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();

            passwordResetTokenRepository.findByUser(user).ifPresent(passwordResetTokenRepository::delete);

            String token = UUID.randomUUID().toString(); // todo verificar que no haya un token repetido en la base

            createPasswordResetTokenForUser(token, user);

            String subject = "Reset password";
            String text = "Reset Password" + " \r\n" + baseUrl + token;

            emailService.sendSimpleMessage(email, subject, text);
        }
    }

    public Long updatePassword(String password, String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken.isEmpty() || passwordResetToken.get().isExpired()) {
           throw new InvalidTokenException("Invalid token");
        }

        AppUser user = appUserRepository.findById(passwordResetToken.get().getUser().getId())
                .orElseThrow(NotFoundException::new);
        passwordResetTokenRepository.delete(passwordResetToken.get());

        user.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(user).getId();
    }

    private void createPasswordResetTokenForUser(String token, AppUser user) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }
}
