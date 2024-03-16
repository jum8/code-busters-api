package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Subscription;
import dev.codebusters.code_busters.domain.UserSubscription;
import dev.codebusters.code_busters.domain.UserType;
import dev.codebusters.code_busters.model.UserSubscriptionDTO;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.SubscriptionRepository;
import dev.codebusters.code_busters.repos.UserSubscriptionRepository;
import dev.codebusters.code_busters.repos.UserTypeRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final AppUserRepository appUserRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final UserTypeRepository userTypeRepository;

    public UserSubscriptionService(final UserSubscriptionRepository userSubscriptionRepository,
                                   final AppUserRepository appUserRepository,
                                   final SubscriptionRepository subscriptionRepository, UserTypeRepository userTypeRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.appUserRepository = appUserRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userTypeRepository = userTypeRepository;
    }

    public List<UserSubscriptionDTO> findAll() {
        final List<UserSubscription> userSubscriptions = userSubscriptionRepository.findAll(Sort.by("id"));
        return userSubscriptions.stream()
                .map(userSubscription -> mapToDTO(userSubscription, new UserSubscriptionDTO()))
                .toList();
    }

    public UserSubscriptionDTO get(final Long id) {
        return userSubscriptionRepository.findById(id)
                .map(userSubscription -> mapToDTO(userSubscription, new UserSubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final UserSubscriptionDTO userSubscriptionDTO) {
        final UserSubscription userSubscription = new UserSubscription();

        mapToEntity(userSubscriptionDTO, userSubscription);

        userSubscription.getUser().setPremium(true); // todo ver si eliminarlo y usar userType

        UserType premiumUserType = userTypeRepository.findByTitle("PREMIUM")
                .orElseThrow(() -> new NotFoundException("userType not found"));

        userSubscription.getUser().setUserType(premiumUserType);

        Optional<LocalDate> maxExpirationDate = userSubscriptionRepository.findMaxExpirationDateByUserId(
                userSubscription.getUser().getId());

        LocalDate startDate = maxExpirationDate.map(date -> date.plusDays(1)).orElseGet(LocalDate::now);

        userSubscription.setStartDate(startDate);
        userSubscription.setExpirationDate(startDate.plusMonths(userSubscription.getSubscription().getDurationInMonths()));

        return userSubscriptionRepository.save(userSubscription).getId();
    }

    public void update(final Long id, final UserSubscriptionDTO userSubscriptionDTO) {
        final UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userSubscriptionDTO, userSubscription);
        userSubscriptionRepository.save(userSubscription);
    }

    public void delete(final Long id) {
        userSubscriptionRepository.deleteById(id);
    }

    private UserSubscriptionDTO mapToDTO(final UserSubscription userSubscription,
                                         final UserSubscriptionDTO userSubscriptionDTO) {
        userSubscriptionDTO.setId(userSubscription.getId());
        userSubscriptionDTO.setStartDate(userSubscription.getStartDate());
        userSubscriptionDTO.setExpirationDate(userSubscription.getExpirationDate());
        userSubscriptionDTO.setUser(userSubscription.getUser() == null ? null : userSubscription.getUser().getId());
        userSubscriptionDTO.setSubscription(userSubscription.getSubscription() == null ? null : userSubscription.getSubscription().getId());
        return userSubscriptionDTO;
    }

    private UserSubscription mapToEntity(final UserSubscriptionDTO userSubscriptionDTO,
                                         final UserSubscription userSubscription) {
        userSubscription.setStartDate(userSubscriptionDTO.getStartDate());
        userSubscription.setExpirationDate(userSubscriptionDTO.getExpirationDate());
        final AppUser user = userSubscriptionDTO.getUser() == null ? null : appUserRepository.findById(userSubscriptionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        userSubscription.setUser(user);
        final Subscription subscription = userSubscriptionDTO.getSubscription() == null ? null : subscriptionRepository.findById(userSubscriptionDTO.getSubscription())
                .orElseThrow(() -> new NotFoundException("subscription not found"));
        userSubscription.setSubscription(subscription);
        return userSubscription;
    }

}