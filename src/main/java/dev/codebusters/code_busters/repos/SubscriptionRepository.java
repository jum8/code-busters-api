package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s, COUNT(us) AS userSubscriptionCount FROM Subscription s " +
            "JOIN UserSubscription us ON s.id = us.subscription.id " +
            "WHERE " +
            "(:dateFrom IS NULL OR us.dateCreated >= :dateFrom) AND " +
            "(:dateTo IS NULL OR us.dateCreated <= :dateTo)" +
            "GROUP BY s.id " +
            "ORDER BY userSubscriptionCount DESC ")
    List<Object[]> findSubscriptionCountBetweenDates(@Param("dateFrom") OffsetDateTime dateFrom,
                                                     @Param("dateTo") OffsetDateTime dateTo);
}

