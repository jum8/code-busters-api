package dev.codebusters.code_busters.service;


import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.UserSubscription;
import dev.codebusters.code_busters.domain.UserType;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.UserSubscriptionRepository;
import dev.codebusters.code_busters.repos.UserTypeRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionExpirationService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final AppUserRepository appUserRepository;

    private final UserTypeRepository userTypeRepository;

    public SubscriptionExpirationService(UserSubscriptionRepository userSubscriptionRepository,
                                         AppUserRepository appUserRepository, UserTypeRepository userTypeRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.appUserRepository = appUserRepository;
        this.userTypeRepository = userTypeRepository;
    }

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkSubscriptionExpiration() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<UserSubscription> expiredSubscriptions = userSubscriptionRepository.findByExpirationDate(today);

        List<UserSubscription> actualExpiredSubscriptions = expiredSubscriptions.stream()
                .filter(subscription -> !userSubscriptionRepository.existsByUserIdAndStartDateEquals(
                            subscription.getUser().getId(), tomorrow))
                .toList();

        UserType userUserType = userTypeRepository.findByTitle("USER")
                .orElseThrow(() -> new NotFoundException("userType not found"));

        for (UserSubscription subscription : actualExpiredSubscriptions) {
            AppUser user = subscription.getUser();
            user.setPremium(false); // todo ver si eliminarlo y usar userType
            user.setUserType(userUserType);
            appUserRepository.save(user);
        }
    }
}
