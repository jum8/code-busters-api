package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}

