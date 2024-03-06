package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.*;
import dev.codebusters.code_busters.model.AppUserDTO;
import dev.codebusters.code_busters.model.auth.UserRegistrationRequest;
import dev.codebusters.code_busters.repos.*;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import dev.codebusters.code_busters.util.ResourceAlreadyExistsException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserTypeRepository userTypeRepository;

    //private final EmailService emailService;
    private final SubmissionRepository submissionRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(final AppUserRepository appUserRepository,
                          final CountryRepository countryRepository, final CityRepository cityRepository, final UserTypeRepository userTypeRepository,
                          final SubmissionRepository submissionRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.userTypeRepository = userTypeRepository;
        this.submissionRepository = submissionRepository;
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

    public Long create(final UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        if (appUserRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email " + email + " is already registered");
        }

        final AppUser appUser = new AppUser();
        mapUserRegistrationRequestToEntity(userRegistrationRequest, appUser);
        appUser.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        /*emailService.sendEmail(user.getEmail(), "¡Bienvenido a Code Busters!", user.getName(), List.of(
                "Gracias por registrarte",
                "Te damos la bienvenida a la mejor plataforma para retos de Ciberseguridad",
                "Esperamos que disfrutes de tu experiencia"
        ));*/
        return appUserRepository.save(appUser).getId();
    }

    /*public String generateResetCode(String email) {

        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // Números aleatorios del 0 al 9
        }
        activeCodes.put(code.toString(), email);

        Timer codeTimer = new Timer();

        codeTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        activeCodes.remove(code.toString());
                        codeTimer.cancel();
                    }
                }, 120000
        );
        return code.toString();


    }

    public void sendResetCode(String email) throws ResourceNotFoundException {
        UserDTO user = findByEmail(email);
        String code = generateResetCode(email);
        emailService.sendEmail(email, "Recupera tu contraseña de Code Busters", user.getName(), List.of(
                "Tu código de recuperación es: " + code
        ));
    }*/

    /*public void resetPassword(String code, String newPassword) throws ResourceNotFoundException {
        String email = activeCodes.get(code);
        if (email == null ) throw new ResourceNotFoundException("Code " + code + " not found");
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User " + email + " not found"));
        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
        activeCodes.remove(code);

    }*/

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
        appUserDTO.setPassword(appUser.getPassword());
        appUserDTO.setEnabled(appUser.getEnabled());
        appUserDTO.setCountry(appUser.getCountry() == null ? null : appUser.getCountry().getId());
        appUserDTO.setCity(appUser.getCity() == null ? null : appUser.getCity().getId());
        appUserDTO.setUserType(appUser.getUserType() == null ? null : appUser.getUserType().getId());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setName(appUserDTO.getName());
        appUser.setAdded(appUserDTO.getAdded());
        appUser.setLastActive(appUserDTO.getLastActive());
        appUser.setPassword(appUserDTO.getPassword());
        appUser.setEnabled(appUserDTO.getEnabled());
        final Country country = appUserDTO.getCountry() == null ? null : countryRepository.findById(appUserDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        appUser.setCountry(country);
        final City city = appUserDTO.getCity() == null ? null : cityRepository.findById(appUserDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        appUser.setCity(city);
        final UserType userType = appUserDTO.getUserType() == null ? null : userTypeRepository.findById(appUserDTO.getUserType())
                .orElseThrow(() -> new NotFoundException("userType not found"));
        appUser.setUserType(userType);
        return appUser;
    }

    private AppUser mapUserRegistrationRequestToEntity(final UserRegistrationRequest userRegistrationRequest, final AppUser appUser) {
        appUser.setEmail(userRegistrationRequest.getEmail());
        appUser.setName(userRegistrationRequest.getName());
        appUser.setPassword(userRegistrationRequest.getPassword());
        final UserType userType = userTypeRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("userType not found"));
        appUser.setUserType(userType); // TODO? ver si cambiar el userType a Enum
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
        return null;
    }

}
