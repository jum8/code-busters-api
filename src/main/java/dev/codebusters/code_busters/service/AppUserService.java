package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.*;
import dev.codebusters.code_busters.model.AppUserDTO;
import dev.codebusters.code_busters.model.OnRegistrationCompleteEvent;
import dev.codebusters.code_busters.model.UserRankingDTO;
import dev.codebusters.code_busters.model.auth.UserRegistrationRequest;
import dev.codebusters.code_busters.repos.*;
import dev.codebusters.code_busters.util.InvalidTokenException;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import dev.codebusters.code_busters.util.ResourceAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserTypeRepository userTypeRepository;

    private final SubmissionRepository submissionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    public AppUserService(final AppUserRepository appUserRepository,
                          final CountryRepository countryRepository, final CityRepository cityRepository,
                          final UserTypeRepository userTypeRepository, final SubmissionRepository submissionRepository,
                          final UserSubscriptionRepository userSubscriptionRepository, SubscriptionRepository subscriptionRepository, final PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
        this.appUserRepository = appUserRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.userTypeRepository = userTypeRepository;
        this.submissionRepository = submissionRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    public List<AppUserDTO> findAll() {
        final List<AppUser> appUsers = appUserRepository.findAll(Sort.by("id"));
        return appUsers.stream()
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .toList();
    }

    @Transactional
    public List<UserRankingDTO> findAllSortedByPoints(Integer limit) {
        final List<AppUser> appUsers = appUserRepository.findAllUsersSortedByPointsWithDefaultLimit(limit);
        return appUsers.stream()
                .map(appUser -> mapToDTO(appUser, new UserRankingDTO()))
                .toList();
    }

    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public AppUser create(final UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        if (appUserRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email " + email + " is already registered");
        }

        final AppUser appUser = new AppUser();
        mapUserRegistrationRequestToEntity(userRegistrationRequest, appUser);
        appUser.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));

        AppUser savedAppUser = appUserRepository.save(appUser);

        Subscription freeSubscription = subscriptionRepository.findByName("Free")
                .orElseThrow(() -> new NotFoundException("Free subscription not found"));
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(savedAppUser);
        userSubscription.setSubscription(freeSubscription);
        userSubscription.setStartDate(LocalDate.now());

        userSubscriptionRepository.save(userSubscription);

        return savedAppUser;
    }

    @Async
    @EventListener
    public void requestRegistrationConfirmation(OnRegistrationCompleteEvent event) {
        AppUser user = event.getUser();
        String token = UUID.randomUUID().toString();

        verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);

        createVerificationTokenForUser(token, user);

        String subject = "Confirmación Code Buster";
        String text = "Estimado/a " + user.getName() +",\n\n" +
                "Gracias por confiar en Code Buster para adentrarte en este apasionado mundo de la ciberseguridad\n\n" +
                "Por favor, haz clic en el siguiente enlace para confirmar tu dirección de correo electrónico:\n\n" +
                event.getAppUrl() + "?token=" + token + "\n\n" +
                "¡Gracias por ser parte de nuestra comunidad!\n\n" +
                "Atentamente,\n" +
                "El equipo de Code Buster";

        emailService.sendSimpleMessage(user.getEmail(), subject, text);

    }

    public Long confirmRegistration(String token) {
        Optional<VerificationToken> passwordResetToken = verificationTokenRepository.findByToken(token);
        if (passwordResetToken.isEmpty() || passwordResetToken.get().isExpired()) {
            throw new InvalidTokenException("Invalid token");
        }

        AppUser user = appUserRepository.findById(passwordResetToken.get().getUser().getId())
                .orElseThrow(NotFoundException::new);
        verificationTokenRepository.delete(passwordResetToken.get());

        user.setEnabled(true);
        return appUserRepository.save(user).getId();
    }

    private void createVerificationTokenForUser(String token, AppUser user) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }


    public void update(final Long id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    public void delete(final Long id) {
        appUserRepository.deleteById(id);
    }



    private AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setName(appUser.getName());
        appUserDTO.setAdded(appUser.getAdded());
        appUserDTO.setLastActive(appUser.getLastActive());
        appUserDTO.setEnabled(appUser.getEnabled());
        appUserDTO.setPremium(appUser.getPremium());
        appUserDTO.setPoints(appUser.getPoints());
        appUserDTO.setProfileImage(appUser.getProfileImage());
        appUserDTO.setCountry(appUser.getCountry() == null ? null : appUser.getCountry().getId());
        appUserDTO.setCity(appUser.getCity() == null ? null : appUser.getCity().getId());
        appUserDTO.setUserType(appUser.getUserType() == null ? null : appUser.getUserType().getId());
        return appUserDTO;
    }
    private UserRankingDTO mapToDTO(final AppUser appUser, final UserRankingDTO userRankingDTO) {
        userRankingDTO.setName(appUser.getName());
        userRankingDTO.setPoints(appUser.getPoints());
        userRankingDTO.setCountry(appUser.getCountry() == null ? null : appUser.getCountry().getCountryName());
        return userRankingDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setName(appUserDTO.getName());
        appUser.setProfileImage(appUserDTO.getProfileImage());
        final Country country = appUserDTO.getCountry() == null ? null : countryRepository.findById(appUserDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        appUser.setCountry(country);
        final City city = appUserDTO.getCity() == null ? null : cityRepository.findById(appUserDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        appUser.setCity(city);
        return appUser;
    }

    private AppUser mapUserRegistrationRequestToEntity(final UserRegistrationRequest userRegistrationRequest, final AppUser appUser) {
        appUser.setEmail(userRegistrationRequest.getEmail());
        appUser.setName(userRegistrationRequest.getName());
        appUser.setPassword(userRegistrationRequest.getPassword());
        final UserType userType = userTypeRepository.findByTitle("USER")
                .orElseThrow(() -> new NotFoundException("userType not found"));
        appUser.setUserType(userType);
        return appUser;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Submission userSubmission = submissionRepository.findFirstByUser(appUser);
        if (userSubmission != null) {
            referencedWarning.setKey("appUser.submission.user.referenced");
            referencedWarning.addParam(userSubmission.getId());
            return referencedWarning;
        }
        final UserSubscription userUserSubscription = userSubscriptionRepository.findFirstByUser(appUser);
        if (userUserSubscription != null) {
            referencedWarning.setKey("appUser.userSubscription.user.referenced");
            referencedWarning.addParam(userUserSubscription.getId());
            return referencedWarning;
        }
        return null;
    }

}
