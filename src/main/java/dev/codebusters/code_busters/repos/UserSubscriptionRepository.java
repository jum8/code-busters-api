package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Subscription;
import dev.codebusters.code_busters.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    UserSubscription findFirstByUser(AppUser appUser);

    UserSubscription findFirstBySubscription(Subscription subscription);

    List<UserSubscription> findByExpirationDate(LocalDate today);
}

