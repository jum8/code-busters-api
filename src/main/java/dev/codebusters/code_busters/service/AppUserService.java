package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.*;
import dev.codebusters.code_busters.model.AppUserDTO;
import dev.codebusters.code_busters.model.auth.UserRegistrationRequest;
import dev.codebusters.code_busters.repos.*;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import dev.codebusters.code_busters.util.ResourceAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class AppUserService {

    @Value("${frontend.url}")
    String baseUrl;
    private final AppUserRepository appUserRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserTypeRepository userTypeRepository;

    private final EmailService emailService;
    private final SubmissionRepository submissionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(final AppUserRepository appUserRepository,
                          final CountryRepository countryRepository, final CityRepository cityRepository,
                          final UserTypeRepository userTypeRepository, EmailService emailService, final SubmissionRepository submissionRepository,
                          final UserSubscriptionRepository userSubscriptionRepository, SubscriptionRepository subscriptionRepository, final PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.userTypeRepository = userTypeRepository;
        this.emailService = emailService;
        this.submissionRepository = submissionRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUserDTO> findAll() {
        final List<AppUser> appUsers = appUserRepository.findAll(Sort.by("id"));
        return appUsers.stream()
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .toList();
    }

    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        if (appUserRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email " + email + " is already registered");
        }

        final AppUser appUser = new AppUser();
        mapUserRegistrationRequestToEntity(userRegistrationRequest, appUser);
        appUser.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));

        // Generar y guardar el token único para el usuario
        String token = UUID.randomUUID().toString();
        appUser.setVerificationToken(token);

        AppUser savedAppUser = appUserRepository.save(appUser);

        // Envío de correo electrónico de verificación
        String path = "/auth/registrationConfirmation?token=";
        String subject = "Verifique su correo";
        String text = "Verifique su correo" + " \r\n" + baseUrl + path + token;
        emailService.sendSimpleMessage(email, subject, text);

        Subscription freeSubscription = subscriptionRepository.findByName("Free")
                .orElseThrow(() -> new NotFoundException("Free subscription not found"));
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(savedAppUser);
        userSubscription.setSubscription(freeSubscription);
        userSubscription.setStartDate(LocalDate.now());

        userSubscriptionRepository.save(userSubscription);

        return savedAppUser.getId();
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
