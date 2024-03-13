package dev.codebusters.code_busters.service;


import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.UserSubscription;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.UserSubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionExpirationService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final AppUserRepository appUserRepository;

    public SubscriptionExpirationService(UserSubscriptionRepository userSubscriptionRepository,
                                         AppUserRepository appUserRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.appUserRepository = appUserRepository;
    }

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkSubscriptionExpiration() {
        LocalDate today = LocalDate.now();

        List<UserSubscription> expiredSubscriptions = userSubscriptionRepository.findByExpirationDate(today);

        for (UserSubscription subscription : expiredSubscriptions) {
            AppUser user = subscription.getUser();
            user.setPremium(false);
            appUserRepository.save(user);
        }
    }
}
