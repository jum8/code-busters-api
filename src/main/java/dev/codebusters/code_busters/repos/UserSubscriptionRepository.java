package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Subscription;
import dev.codebusters.code_busters.domain.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    UserSubscription findFirstByUser(AppUser appUser);

    UserSubscription findFirstBySubscription(Subscription subscription);

    List<UserSubscription> findByExpirationDate(LocalDate today);


    @Query("SELECT MAX(us.expirationDate) FROM UserSubscription us WHERE "
            + "us.user.id = :userId")
    Optional<LocalDate> findMaxExpirationDateByUserId(@Param("userId") Long userId);

    Boolean existsByUserIdAndStartDateEquals(Long userId, LocalDate startDate);
}

