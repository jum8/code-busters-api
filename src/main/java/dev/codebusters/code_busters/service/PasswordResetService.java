package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.config.FrontendConfig;
import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.VerificationToken;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.VerificationTokenRepository;
import dev.codebusters.code_busters.util.InvalidTokenException;
import dev.codebusters.code_busters.util.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final FrontendConfig frontendConfig;

    public PasswordResetService(VerificationTokenRepository verificationTokenRepository, AppUserRepository appUserRepository, EmailService emailService, PasswordEncoder passwordEncoder, FrontendConfig frontendConfig) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.frontendConfig = frontendConfig;
    }

    public void forgotPassword(String email) {
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();

            verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);

            String token = UUID.randomUUID().toString(); // todo verificar que no haya un token repetido en la base

            createPasswordResetTokenForUser(token, user);

            String subject = "Resetear contraseña - Code Buster";
            String text = "Estimado/a " + user.getName() +",\n\n" +
                    "Recibimos una solicitud para restablecer la contraseña de tu cuenta en Code Buster.\n" +
                    "Para completar este proceso, sigue las instrucciones detalladas a continuación:\n\n" +
                    "\t-\tHaz clic en el siguiente enlace para iniciar el proceso de reseteo de contraseña:\n\n" +
                    frontendConfig.getUrl() + frontendConfig.getChangePasswordPath() + "?token=" + token + "\n\n" +
                    "\t-\tSigue las instrucciones en pantalla para elegir una nueva contraseña segura para tu cuenta.\n\n" +
                    "Si no has solicitado un reseteo de contraseña, por favor ignora este mensaje.\n\n" +
                    "Atentamente,\n" +
                    "El equipo de Code Buster";

            emailService.sendSimpleMessage(email, subject, text);
        }
    }

    public Long updatePassword(String password, String token) {
        Optional<VerificationToken> passwordResetToken = verificationTokenRepository.findByToken(token);
        if (passwordResetToken.isEmpty() || passwordResetToken.get().isExpired()) {
           throw new InvalidTokenException("Invalid token");
        }

        AppUser user = appUserRepository.findById(passwordResetToken.get().getUser().getId())
                .orElseThrow(NotFoundException::new);
        verificationTokenRepository.delete(passwordResetToken.get());

        user.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(user).getId();
    }

    private void createPasswordResetTokenForUser(String token, AppUser user) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }
}
